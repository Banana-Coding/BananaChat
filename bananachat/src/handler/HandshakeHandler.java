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

package handler;

import knuddels.Client;
import tools.PacketCreator;

/**
 * 
 * @author Flav
 * @since 1.0
 */
public class HandshakeHandler {
	public static void handle(String[] tokens, Client client) {
		client.send(PacketCreator.butler());
		client.sendHello();
	}
}
