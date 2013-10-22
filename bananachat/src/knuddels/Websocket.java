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
import java.util.Collection;
import java.util.Iterator;

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
		Client c = Server.get().getClient(socket);
		c.disconnect();
		
		Server.get().removeClient(socket);
	}

	@Override
	public void onError(WebSocket socket, Exception e) {
		/* Do Nothing */
		e.printStackTrace();
	}

	@Override
	public void onMessage(WebSocket socket, String decoded) {
		SessionHandler handler	= new SessionHandler(null);
		Client client			= Server.get().getClient(socket);
		
		// Client does not exists
		if(client == null) {
			client = new Client(null);
			client.setToWebsocket(socket);
			Server.get().addClient(client);
		}
		
		handler.read(client, decoded);
	}

	@Override
	public void onOpen(WebSocket socket, ClientHandshake arg1) {
		/* Do Nothing */
	}
}
