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

package knuddels.game;

/**
 * 
 * @author Flav
 */
public class MafiaPlayer {
	private String name;
	private MafiaCharacter character;

	public MafiaPlayer(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public MafiaCharacter getCharacter() {
		return character;
	}

	public void setCharacter(MafiaCharacter character) {
		this.character = character;
	}
}
