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
import knuddels.CommandParser;
import knuddels.Server;

/**
 *
 * @author Flav
 */
public class ChatHandler {
    public static void handle(String[] tokens, Client client) {
        Channel channel = Server.get().getChannel(tokens[1]);

        if (channel == null || !channel.getClients().contains(client)) {
            return;
        }

        if (client.isAway()) {
            client.setAway(false);
        }

        if (!client.isModerator()) {
            if (client.checkSpam(channel.getName())) {
                return;
            }
        }

        String message = tokens[2].trim();

        if (message.isEmpty()) {
            return;
        }

        if (message.charAt(0) == '/') {
            CommandParser.parse(message, client, channel);
        } else {
            channel.broadcastMessage(message, client, false);
        }
    }
}
