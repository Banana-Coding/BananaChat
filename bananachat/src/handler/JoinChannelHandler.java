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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import knuddels.Channel;
import knuddels.Client;
import knuddels.Server;
import tools.HexTool;
import tools.PacketCreator;
import tools.database.ConnectionPool;
import tools.database.PoolConnection;
import tools.popup.Popup;

/**
 * 
 * @author Flav
 * @since 1.0
 */
public class JoinChannelHandler {
	public static void handle(String[] tokens, Client client) {
		String nickname = tokens[2];

		if (nickname.isEmpty()) {
			client.send(Popup
					.create("Problem",
							"Nickname fehlt",
							"#Um in den Chat eintreten zu können, müssen Sie _vorher einen Nick registrieren_.##Klicken Sie dazu auf der Webseite auf folgenden Button:##°>neu_reg.gif<°",
							400, 300));
			return;
		}

		PoolConnection pcon = ConnectionPool.getConnection();
		PreparedStatement ps = null;

		try {
			Connection con = pcon.connect();
			ps = con.prepareStatement("SELECT `name`, `password` FROM `accounts` WHERE `name` = ?");
			ps.setString(1, nickname);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				client.send(Popup.create(
						"Problem",
						"Nick existiert nicht",
						String.format(
								"#Der Nickname '%s' ist _nicht registriert_.##Um diesen Nicknamen zu registrieren klicken Sie auf der Webseite auf folgenden Button:##°>neu_reg.gif<°",
								nickname), 400, 300));
				return;
			}

			if (!HexTool.hash("SHA1", tokens[3]).equals(
					rs.getString("password"))) {
				client.send(Popup.create(
						"Problem",
						"Falsches Passwort",
						String.format(
								"#_Falsches Passwort_ für %s verwendet. Achten Sie auf mögliche Groß-/Kleinschreibung Ihres Passwortes.##°B>Passwort vergessen/funktioniert nicht mehr?|http://www.knuddels.de/pwd.html<r°##Um einen neuen Nicknamen zu registrieren, klicken Sie auf der Webseite den folgenden Button an:##°>neu_reg.gif<°",
								rs.getString("name")), 400, 300));
				return;
			}

			client.login(nickname);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}

			pcon.close();
		}

		if (client.isAway()) {
			client.setAway(false);
		}

		Channel channel = Server.get().getChannel(tokens[1]);

		if (channel == null) {
			client.send(Popup.create("Problem", "Channellogin nicht möglich",
					String.format("Der Channel _%s existiert nicht_.",
							tokens[1]), 400, 300));
			return;
		}

		if (channel.getClients().contains(client)) {
			return;
		}

		if (!client.isModerator()
				&& channel.countClients() == channel.getSize()) {
			client.send(Popup.create(
					"Problem",
					"Problem",
					String.format(
							"Dieser Channel ist auf _maximal %s_ Leute beschränkt, bitte wähl einen anderen Channel.",
							channel.getSize()), 400, 300));
			return;
		}

		client.joinChannel(channel);
		client.send(PacketCreator.channelFrame(channel, client.getName()));
		channel.join(client);
	}
}
