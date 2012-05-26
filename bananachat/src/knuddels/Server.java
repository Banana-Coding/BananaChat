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

package knuddels;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import tools.database.ConnectionPool;
import tools.database.PoolConnection;

/**
 * 
 * @author Flav
 */
public class Server {
	private final static Server instance;
	private final Map<String, Client> clients;
	private Client butler;
	private Map<String, Channel> channels;
	private Map<String, String> smileys;

	static {
		instance = new Server();
	}

	public Server() {
		clients = new HashMap<String, Client>();
		butler = new Client(null);
		channels = new LinkedHashMap<String, Channel>();
		smileys = new HashMap<String, String>();
	}

	public static Server get() {
		return instance;
	}

	public Client getButler() {
		return butler;
	}

	public Client getClient(String name) {
		synchronized (clients) {
			return clients.get(name.toLowerCase());
		}
	}

	public Collection<Client> getClients() {
		synchronized (clients) {
			return clients.values();
		}
	}

	public void addClient(Client client) {
		synchronized (clients) {
			clients.put(client.getName().toLowerCase(), client);
		}
	}

	public void removeClient(String name) {
		synchronized (clients) {
			clients.remove(name.toLowerCase());
		}
	}

	public Channel getChannel(String name) {
		return channels.get(name.toLowerCase());
	}

	public Collection<Channel> getChannels() {
		return channels.values();
	}

	public String parseSmileys(String message) {
		Iterator<String> it = smileys.keySet().iterator();

		while (it.hasNext()) {
			String code = it.next();
			message = message.replace(code,
					String.format("°>%s<°", smileys.get(code)));
		}

		return message;
	}

	private void loadConfigs() {
		PoolConnection pcon = ConnectionPool.getConnection();
		Statement stmt = null;

		try {
			Connection con = pcon.connect();
			stmt = con.createStatement();
			System.out.println("Loading butler");
			ResultSet rs = stmt
					.executeQuery("SELECT `name` FROM `accounts` ORDER BY `id` LIMIT 1");
			rs.next();
			butler.login(rs.getString("name"));
			rs.close();
			System.out.println("Loading channel styles");
			rs = stmt.executeQuery("SELECT * FROM `channelstyles`");
			Map<Integer, ChannelStyle> channelStyles = new HashMap<Integer, ChannelStyle>();

			while (rs.next()) {
				channelStyles.put(rs.getInt("id"), new ChannelStyle(rs));
			}

			rs.close();
			System.out.println("Loading channels");
			rs = stmt.executeQuery("SELECT * FROM `channels`");

			while (rs.next()) {
				Channel channel = new Channel(rs, channelStyles.get(rs
						.getInt("style")));
				butler.joinChannel(channel);
				channel.addClient(butler);
				channels.put(rs.getString("name").toLowerCase(), channel);
			}

			rs.close();
			System.out.println("Loading smileys");
			rs = stmt
					.executeQuery("SELECT `code`, `replacement` FROM `smileys`");

			while (rs.next()) {
				smileys.put(rs.getString("code"), rs.getString("replacement"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}

			pcon.close();
		}
	}

	private void listen(int port) {
		try {
			ServerSocket listener = new ServerSocket(port);
			System.out.println(String.format("Listening on port %s", port));

			while (true) {
				Socket socket = listener.accept();
				new SessionHandler(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int port;

		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 2710; // default
		}

		instance.loadConfigs();
		instance.listen(port);
	}
}
