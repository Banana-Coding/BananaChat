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

package tools.popup;

/**
 * 
 * @author Flav
 * @since 1.0
 */
public enum ComponentType {
	BUTTON('b'), TEXT_FIELD('f'), LABEL('l'), TEXT_AREA('t'), CHECKBOX('x');

	private int type;

	private ComponentType(int type) {
		this.type = type;
	}

	public int getValue() {
		return type;
	}
}
