/* Banana-Chat - The first Open Source Knuddels Emulator
 * Copyright (C) 2011  Flav <http://banana-coding.com>
 *
 * Diese Datei unterliegt dem Copyright von Banana-Coding und
 * darf ver�ndert, aber weder in andere Projekte eingef�gt noch
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
 */
public interface Component {
	public ComponentType getType();

	public int[] getForeground();

	public void setForeground(int[] foreground);

	public int[] getBackground();

	public void setBackground(int[] background);

	public String getText();
}
