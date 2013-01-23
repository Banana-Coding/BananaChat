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

package knuddels;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Flav
 */
public class ChannelStyle {
	private byte fontSize;
	private byte lineSpace;
	private String foreground, background, red, blue, administrator, moderator;

	public ChannelStyle(ResultSet rs) throws SQLException {
		fontSize = rs.getByte("fontSize");
		lineSpace = rs.getByte("lineSpace");
		foreground = rs.getString("foreground");
		background = rs.getString("background");
		red = rs.getString("red");
		blue = rs.getString("blue");
		administrator = rs.getString("administrator");
		moderator = rs.getString("moderator");
	}

	public byte getFontSize() {
		return fontSize;
	}

	public byte getLineSpace() {
		return lineSpace;
	}

	public String getForeground() {
		return foreground;
	}

	public String getBackground() {
		return background;
	}

	public String getRed() {
		return red;
	}

	public String getBlue() {
		return blue;
	}

	public String getRankColor(Client client) {
		if (client.isAdministrator()) {
			return administrator;
		}

		if (client.isModerator()) {
			return moderator;
		}

		return foreground;
	}
}
