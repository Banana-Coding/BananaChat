/* Banana-Chat - The first Open Source Knuddels Emulator
 * Copyright (C) 2011-2013  Flav <http://banana-coding.com>
 *
 * Diese Datei unterliegt dem Copyright von Banana-Coding und
 * darf verändert, aber weder in andere Projekte eingefügt noch
 * reproduziert werden.
 *
 * Der Emulator dient - sofern der Client nicht aus Eigenproduktion
 * stammt - nur zu Lernzwecken, das Hosten des originalen Clients
 * ist untersagt und wird der Knuddels GmbH gemeldet.
 */

package knuddels;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Bizzi
 * @since 1.1
 */
public class FlashPolicyServer {
    private static ServerSocket serverSock;
    private static boolean listening = true;
    private static Thread serverThread;
    
    public  void run() {
    	serverThread = new Thread(new Runnable(){
    		public void run() {
    			try {
    				System.out.println("Starting FlashPolicyServer");
    				
    				try {
    					serverSock = new ServerSocket(843, 50);
    				} catch(Exception e) {
    					System.out.println("FlashPolicyServer on PORT 2706");
    					serverSock = new ServerSocket(2706, 50);
    				}
    				
    				while(listening) {
    					final Socket sock	= serverSock.accept();
    					Thread t			= new Thread(new Runnable() {
    						public void run() {
    							try {
    								sock.setSoTimeout(10000);
    								InputStream in = sock.getInputStream();
    								byte[] buffer = new byte[4096];
    								
    								if(in.read(buffer) != -1 && (new String(buffer)).startsWith("<policy-file-request/>")) {
    									String flashPolicy = "<cross-domain-policy>\n<allow-access-from domain=\"*\" to-ports=\"*\" />\n</cross-domain-policy>";
										OutputStream out = sock.getOutputStream();
										out.write(flashPolicy.getBytes());
										out.write(0x00);
										out.flush();
										out.close();
									} else {
										System.out.println("PolicyServer: Ignoring Invalid Request");
										System.out.println("  " + (new String(buffer)));
									}
    							}  catch (Exception ex) {
    								System.out.println("PolicyServer: Error: " + ex.toString());
    							} finally {
    								try {
    									sock.close();
    								} catch (Exception ex2) {
    									/* Do Nothing */
    								}
    							}
    						}
    					});
    					
    					t.start();
    				}
    			} catch (Exception ex) {
    				System.out.println("PolicyServerServlet: Error: " + ex.toString());
    			}
    		}
        });
    	
    	serverThread.start();
    }
    
    public void destroy() {
    	if(listening) {
    		listening = false;
    	}
    	
    	if(!serverSock.isClosed()) {
    		try {
    			serverSock.close();
    		} catch (Exception ex) {
    			/* Do Nothing */
    		}
    	}
    }
}