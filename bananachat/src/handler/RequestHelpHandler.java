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

import knuddels.Client;
import tools.PacketCreator;

/**
 *
 * @author Flav
 */
public class RequestHelpHandler {
    public static void handle(String[] tokens, Client client) {
        if (tokens[1].equals("login")) {
            client.send(PacketCreator.openURL("http://www2.knuddels.de/dprint/dprint.pl?jig=4&domain=Knuddels.de", "_blank"));
        }
    }
}
