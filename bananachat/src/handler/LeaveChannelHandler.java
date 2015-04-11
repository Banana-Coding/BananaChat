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

package handler;
import knuddels.Channel;
import knuddels.Client;
import knuddels.Server;

/**
 * 
 * @author Flav
 * @since 1.0
 */
public class LeaveChannelHandler {
	public static void handle(String[] tokens, Client client) {
		Channel channel = Server.get().getChannel(tokens[1]);

		if(channel == null || !channel.getClients().contains(client)) {
			return;
		}

		channel.leave(client);
		client.leaveChannel(channel);
	}
}
