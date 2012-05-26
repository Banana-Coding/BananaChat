/* Banana-Chat - The first Open Source Knuddels Emulator
 * Copyright (C) 2011  Flav <http://banana-coding.com>
 *
 * Diese Datei unterliegt dem Copyright von Banana-Coding und
 * darf verändert, aber weder in andere Projekte eingefügt noch
 * reproduziert werden.
 *
 * Der Emulator dient - sofern der Client nicht aus Eigenproduktion
 * stammt - nur zu Lernzwecken, das Hosten des originalen Clients
 * ist untersagt und wird der Knuddels GmbH gemeldet.
 */

package handler;

import knuddels.Channel;
import knuddels.Client;
import knuddels.Server;

/**
 * 
 * @author Flav
 */
public class LeaveChannelHandler {
	public static void handle(String[] tokens, Client client) {
		Channel channel = Server.get().getChannel(tokens[1]);

		if (channel == null || !channel.getClients().contains(client)) {
			return;
		}

		channel.leave(client);
		client.leaveChannel(channel);

		// if (tokens.length >= 4) {
		// client.updatePosition(tokens[2], tokens[3]);
		//
		// if (tokens.length >= 6) {
		// client.updateSize(tokens[4], tokens[5]);
		//
		// if (tokens.length >= 7) {
		// client.updateScrollspeed(tokens[6]);
		// }
		// }
		// }
	}
}
