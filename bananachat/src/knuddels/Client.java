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

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.java_websocket.WebSocket;

import tools.KCodeParser;
import tools.PacketCreator;
import tools.Pair;
import tools.database.ConnectionPool;
import tools.database.PoolConnection;
import tools.huffman.Huffman;

/**
 * 
 * @author Flav
 * @since 1.0
 */
public class Client {
	private List<Channel> channels;
	private Socket socket;
	private WebSocket websocket;
	private OutputStream out;
	private List<Pair<String, Integer>> icons;
	private String name;
	private byte rank;
	private byte gender;
	private int kisses;
	private int knuddels;
	private int onlineTime;
	private long loginTimestamp;
	private String registrationDate;
	private String registrationTime;
	private String ipAddress;
	private boolean away;
	private int spam;
	private long lastAction;

	public Client(Socket socket) {
		channels = new ArrayList<Channel>();
		this.socket = socket;
		this.websocket = null;
		
		if (socket == null) {
			return;
		}

		try {
			out = socket.getOutputStream();
		} catch (IOException e) {
		}
	}
	
	public void setToWebsocket(WebSocket socket) {
		channels = new ArrayList<Channel>();
		this.socket = null;
		this.websocket = socket;

		if (websocket == null) {
			return;
		}

		/*try {
			out = websocket;
		} catch (IOException e) {
		}*/
	}

	public List<Pair<String, Integer>> getIcons() {
		return icons;
	}

	private void addIcon(String icon, int size) {
		Pair<String, Integer> pair = new Pair<String, Integer>(icon, size);
		icons.add(pair);

		for (Channel channel : getChannels()) {
			String add = PacketCreator.addIcon(channel.getName(), name, pair);

			for (Client target : channel.getClients()) {
				target.send(add);
			}
		}
	}

	private void removeIcon(String icon) {
		for (Pair<String, Integer> pair : icons) {
			if (pair.getLeft().equals(icon)) {
				icons.remove(pair);
				break;
			}
		}

		for (Channel channel : getChannels()) {
			String remove = PacketCreator.removeIcon(channel.getName(), name,
					icon);

			for (Client target : channel.getClients()) {
				target.send(remove);
			}
		}
	}

	public String getName() {
		return name;
	}

	public byte getRank() {
		return rank;
	}

	private boolean isVIP() {
		return rank == 1;
	}

	public boolean isModerator() {
		return rank >= 2;
	}

	public boolean isAdministrator() {
		return rank >= 3;
	}

	public byte getGender() {
		return gender;
	}

	public int getKisses() {
		return kisses;
	}

	public void increaseKisses() {
		kisses++;
	}

	public int getKnuddels() {
		return knuddels;
	}

	public void setKnuddels(int knuddels) {
		this.knuddels = knuddels;
	}

	public int getOnlineTime() {
		return onlineTime
				+ (int) ((System.currentTimeMillis() - loginTimestamp) / 1000);
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public String getRegistrationTime() {
		return registrationTime;
	}

	public String getIPAddress() {
		return ipAddress;
	}

	public boolean isAway() {
		return away;
	}

	public void setAway(boolean away) {
		this.away = away;

		if (away) {
			addIcon("pics/away.png", 20);
		} else {
			removeIcon("pics/away.png");
		}
	}

	public boolean checkSpam(String channel) {
		long now = System.currentTimeMillis();

		if (lastAction > now - 2000) {
			spam++;

			if (spam == 2) {
				sendButlerMessage(channel, "Bitte _NICHT spammen_ und fluten.");
			} else if (spam == 3) {
				disconnect();
				return true;
			}
		} else {
			spam -= (now - lastAction) / 2000;

			if (spam < 0) {
				spam = 0;
			}
		}

		lastAction = now;
		return false;
	}

	public void login(String nickname) throws SQLException {
		if (name != null && !name.equals(nickname)) { // logging in using
														// another account
			logout("Ausgeloggt");
		}

		synchronized (channels) {
			if (channels.isEmpty()) { // is logged out
				Client client = Server.get().getClient(nickname);

				if (client != null) { // someone else is logged into that
										// account
					client.disconnect();
				}

				loadStats(nickname);
			}
		}
	}

	public void logout(String message) {
		synchronized (channels) {
			if (!channels.isEmpty()) { // is logged in
				for (Channel channel : channels) {
					channel.leave(this);

					if (message != null) {
						send(PacketCreator.kick(channel.getName(), message));
					}
				}

				channels.clear();
				saveStats();
				Server.get().removeClient(name);
				sendHello();
			}
		}
	}

	public Channel getChannel() {
		if (name.equals(Server.get().getButler().getName())) {
			return null;
		}

		synchronized (channels) {
			return channels.get(channels.size() - 1);
		}
	}

	public List<Channel> getChannels() {
		synchronized (channels) {
			return channels;
		}
	}

	public void joinChannel(Channel channel) {
		synchronized (channels) {
			if (channels.isEmpty()) { // logged in
				Server.get().addClient(this);
			}

			channels.add(channel);
		}
	}

	public void leaveChannel(Channel channel) {
		synchronized (channels) {
			channels.remove(channel);

			if (channels.isEmpty()) { // logged out
				saveStats();
				Server.get().removeClient(name);
				sendHello();
			}
		}
	}

	public void loadStats(String name) {
		PoolConnection pcon = ConnectionPool.getConnection();
		PreparedStatement ps = null;

		try {
			Connection con = pcon.connect();
			ps = con.prepareStatement("SELECT * FROM `accounts` WHERE `name` = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				this.name = rs.getString("name");
				rank = rs.getByte("rank");

				icons = new ArrayList<Pair<String, Integer>>();
				gender = rs.getByte("gender");

				if (gender == 1) {
					addIcon("pics/male.png", 16);
				} else if (gender == 2) {
					addIcon("pics/female.png", 14);
				}

				if (isVIP()) {
					addIcon("pics/icon_vip.png", 22);
				}

				kisses = rs.getInt("kisses");
				knuddels = rs.getInt("knuddels");
				onlineTime = rs.getInt("onlineTime");
				loginTimestamp = System.currentTimeMillis();

				String[] registration = rs.getString("registration").split(" ");
				String[] date = registration[0].split("-");
				registrationDate = String.format("%s.%s.%s", date[2], date[1],
						date[0]);
				registrationTime = registration[1].split("\\.")[0];

				if (isModerator()) {
					ipAddress = "1.3.3.7";
				} else {
					if (socket == null) {
						ipAddress = rs.getString("ipAddress");
					} else {
						ipAddress = socket.getInetAddress().getHostAddress();
					}
				}

				away = false;
				spam = 0;
				lastAction = 0;
			}
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
	}

	private void saveStats() {
		PoolConnection pcon = ConnectionPool.getConnection();
		PreparedStatement ps = null;

		try {
			Connection con = pcon.connect();
			ps = con.prepareStatement("UPDATE `accounts` SET `kisses` = ?, `knuddels` = ?, `onlineTime` = ?, `ipAddress` = ? WHERE `name` = ?");
			ps.setInt(1, kisses);
			ps.setInt(2, knuddels);
			ps.setInt(3, getOnlineTime());
			ps.setString(4, ipAddress);
			ps.setString(5, name);
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
	}

	public void sendHello() {
		Collection<Channel> channelList = Server.get().getChannels();
		send(PacketCreator
				.hello(((Channel) channelList.toArray()[0]).getName()));
		send(PacketCreator.channelList(channelList));
	}

	public void sendPrivateMessage(List<Client> targets, String message,
			Channel channel, boolean fromGame) {
		if (!fromGame
				&& channel.getGame() != null
				&& !channel.getGame().parsePrivateMessage(targets, message,
						this)) {
			return;
		}

		message = KCodeParser.parse(message, !isModerator(), 5, 10, 20);
		message = Server.get().parseSmileys(message);

		if (message.isEmpty()) {
			return;
		}

		StringBuilder recipients = new StringBuilder();

		for (Client target : targets) {
			recipients.append(target.getName());
			recipients.append(",");
		}

		String recipient = recipients.substring(0, recipients.length() - 1);

		if (!targets.contains(this)) {
			targets.add(this);
		}

		for (Client target : targets) {
			if (channel.getClients().contains(target)) {
				target.send(PacketCreator.privateMessage(name, recipient,
						channel.getName(), message, " "));
			} else {
				for (Channel ch : target.getChannels()) {
					target.send(PacketCreator.privateMessage(name, recipient,
							ch.getName(), message, channel.getName()));
				}
			}
		}
	}

	public void sendButlerMessage(String channel, String message) {
		send(PacketCreator.privateMessage(Server.get().getButler().getName(),
				name, channel, message, " "));
	}

	public void send(String message) {
		if (socket != null && socket.isConnected()) {
			try {
				out.write(Protocol.encode(Huffman.getEncoder().encode(message,
						0)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void disconnect() {
		logout(null);

		if (socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean hasWebsocket(WebSocket socket) {
		if(socket != null && socket == this.websocket) {
			return true;
		}
		
		return false;
	}
}
