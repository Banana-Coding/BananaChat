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
public class Label implements Component {
	private int[] foreground, background;
	private String text;

	public Label(String text) {
		foreground = new int[] { 0x00, 0x00, 0x00 };
		background = new int[] { 0xBE, 0xBC, 0xFB };
		this.text = text;
	}

	public ComponentType getType() {
		return ComponentType.LABEL;
	}

	public int[] getForeground() {
		return foreground;
	}

	public void setForeground(int[] foreground) {
		this.foreground = foreground;
	}

	public int[] getBackground() {
		return background;
	}

	public void setBackground(int[] background) {
		this.background = background;
	}

	public String getText() {
		return text;
	}
}
