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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import knuddels.game.Game;
import knuddels.game.Mafia;
import tools.KCodeParser;
import tools.PacketCreator;
import tools.database.ConnectionPool;
import tools.database.PoolConnection;

/**
 * 
 * @author Flav
 * @since 1.0
 */
public class Channel {
	private final List<Client> clients;
	private String name;
	private short size;
	private String topic;
	private Game game;
	private String backgroundImage;
	private byte backgroundPosition;
	private ChannelStyle style;

	public Channel(ResultSet rs, ChannelStyle style) throws SQLException {
		clients = new ArrayList<Client>();
		name = rs.getString("name");
		size = rs.getShort("size");
		topic = rs.getString("topic");
		String gameName = rs.getString("game");

		if (gameName == null) {
			game = null;
		} else if (gameName.equals("MAFIA")) {
			game = new Mafia(this);
		}

		backgroundImage = rs.getString("backgroundImage");
		backgroundPosition = rs.getByte("backgroundPosition");
		this.style = style;
	}

	public String getName() {
		return name;
	}

	public short getSize() {
		return size;
	}

	public void setTopic(String topic) {
		this.topic = topic;
		PoolConnection pcon = ConnectionPool.getConnection();
		PreparedStatement ps = null;

		try {
			Connection con = pcon.connect();
			ps = con.prepareStatement("UPDATE `channels` SET `topic` = ?  WHERE `name` = ?");
			ps.setString(1, topic);
			ps.setString(2, name);
			ps.executeUpdate();
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

		if (topic != null) {
			for (Client client : getClients()) {
				client.sendButlerMessage(
						name,
						String.format(
								"∞BB∞_Dieser Channel hat folgendes Thema:∞r∞#%s",
								topic));
			}
		}
	}

	public Game getGame() {
		return game;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public byte getBackgroundPosition() {
		return backgroundPosition;
	}

	public ChannelStyle getStyle() {
		return style;
	}

	public void broadcastMessage(String message, Client client, boolean fromGame) {
		if (!fromGame && game != null
				&& !game.parsePublicMessage(message, client)) {
			return;
		}

		message = KCodeParser.parse(message, !client.isModerator(), 5, 10, 20);
		message = Server.get().parseSmileys(message);

		if (message.isEmpty()) {
			return;
		}

		String msg = PacketCreator.publicMessage(client.getName(), name,
				message);

		for (Client c : getClients()) {
			c.send(msg);
		}
	}

	public void broadcastButlerMessage(String message) {
		String msg = PacketCreator.publicMessage(Server.get().getButler()
				.getName(), name, message);

		for (Client c : getClients()) {
			c.send(msg);
		}
	}

	public void broadcastAction(String sender, String message) {
		String action = PacketCreator.action(sender, name, message);

		for (Client c : getClients()) {
			c.send(action);
		}
	}

	public int countClients() {
		int count = getClients().size();

		if (count > size) {
			count = size;
		}

		return count;
	}

	public List<Client> getClients() {
		synchronized (clients) {
			return clients;
		}
	}

	public void addClient(Client client) {
		synchronized (clients) {
			clients.add(client);
		}
	}

	public void join(Client client) {
		client.send(PacketCreator.userList(this));
		addClient(client);

		for (Client c : getClients()) {
			c.send(PacketCreator.addUser(this, client, c == client));
		}

		if (topic != null) {
			client.sendButlerMessage(name, String.format(
					"∞BB∞_Dieser Channel hat folgendes Thema:∞r∞#%s", topic));
		}
	}

	public void leave(Client client) {
		synchronized (clients) {
			clients.remove(client);
		}

		String leave = PacketCreator.removeUser(client.getName(), name);

		for (Client c : getClients()) {
			c.send(leave);
		}

		if (game != null) {
			game.onLeave(client);
		}
	}
}
