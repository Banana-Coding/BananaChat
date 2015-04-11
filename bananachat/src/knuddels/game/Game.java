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

package knuddels.game;
import java.util.List;
import knuddels.Client;

/**
 * 
 * @author Flav
 * @since 1.0
 */
public interface Game {
	public boolean parsePublicMessage(String message, Client client);
	public boolean parsePrivateMessage(List<Client> targets, String message, Client client);
	public void onLeave(Client client);
}
