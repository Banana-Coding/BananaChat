/* Banana-Chat - The first Open Source Knuddels Emulator
 * Copyright (C) 2011-2013  Flav <http://banana-coding.com>
 *
 * Diese Datei unterliegt dem Copyright von Banana-Coding und
 * darf ver�ndert, aber weder in andere Projekte eingef�gt noch
 * reproduziert werden.
 *
 * Der Emulator dient - sofern der Client nicht aus Eigenproduktion
 * stammt - nur zu Lernzwecken, das Hosten des originalen Clients
 * ist untersagt und wird der Knuddels GmbH gemeldet.
 */

package knuddels;
import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * 
 * @author Bizzi
 * @since 1.0
 */
public class Websocket extends WebSocketServer {
	public Websocket(int port) throws Exception {
        super(new InetSocketAddress(port));
    }

	@Override
	public void onClose(WebSocket socket, int arg1, String arg2, boolean arg3) {
		for(Client c : Server.get().getClients()) {
			if(c.hasWebsocket(socket)) {
				c.disconnect();
			}
		}
	}

	@Override
	public void onError(WebSocket socket, Exception arg1) {
		/* Do Nothing */
	}

	@Override
	public void onMessage(WebSocket socket, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onOpen(WebSocket socket, ClientHandshake arg1) {
		// TODO Auto-generated method stub
	}
}
