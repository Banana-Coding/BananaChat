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

package tools;
import java.util.Collection;
import knuddels.Channel;
import knuddels.ChannelStyle;
import knuddels.Client;
import knuddels.SendOpcode;
import knuddels.Server;

/**
 * 
 * @author Flav
 * @since 1.0
 */
public class PacketCreator {
	public static String playMp3(String stream) {
		PacketBuilder buffer = new PacketBuilder(SendOpcode.PLAY_MP3.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(stream);

		return buffer.toString();
	}

	public static String hello(String channel) {
		PacketBuilder buffer = new PacketBuilder(SendOpcode.HELLO.getValue());

		buffer.writeByte(0x00);
		buffer.writeString("xyz"); // password key
		buffer.writeByte(0x00);
		buffer.writeString(channel);

		return buffer.toString();
	}

	public static String pong() {
		PacketBuilder buffer = new PacketBuilder(SendOpcode.PONG.getValue());
		return buffer.toString();
	}

	private static void addChannelStyle(PacketBuilder buffer, ChannelStyle style) {
		buffer.writeByte(0x00);
		buffer.writeString(style.getForeground());
		buffer.writeByte(0x00);
		buffer.writeString(style.getBackground());
		buffer.writeByte(0x00);
		buffer.writeString(style.getRed());
		buffer.writeByte(0x00);
		buffer.writeString(style.getBlue());
		buffer.writeByte(0x00);
		buffer.writeString(String.valueOf(style.getFontSize()));
		buffer.writeByte(0x00);
		buffer.writeString(String.valueOf(style.getLineSpace()));
		buffer.writeByte(0x00);
		buffer.writeString(String.valueOf(style.getFontSize())); // user list
		buffer.writeByte(0x00);
		buffer.writeString(style.getBackground()); // user list
		buffer.writeByte(0x00);
		buffer.writeString("T");
		buffer.writeByte(0x00);
		buffer.writeString("-");
	}

	public static String updateChannelSettings(Channel channel) {
		PacketBuilder buffer = new PacketBuilder(
				SendOpcode.UPDATE_CHANNEL_SETTINGS.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(channel.getName());
		addChannelStyle(buffer, channel.getStyle());

		return buffer.toString();
	}

	public static String butler() {
		PacketBuilder buffer = new PacketBuilder(SendOpcode.BUTLER.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(Server.get().getButler().getName());

		return buffer.toString();
	}

	public static String kick(String channel, String reason) {
		PacketBuilder buffer = new PacketBuilder(SendOpcode.KICK.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(channel);
		buffer.writeByte(0x00);
		buffer.writeString(reason);

		return buffer.toString();
	}

	public static String privateChat(String channel, String nickname) {
		PacketBuilder buffer = new PacketBuilder(SendOpcode.MODULE.getValue());

		buffer.writeByte(0x00);
		buffer.writeByte(0x00);
		buffer.writeByte(0xAE);
		buffer.writeString(channel, true, false);

		for (int i = 0; i < 2; i++) {
			buffer.writeString(nickname, true, false);
		}

		buffer.writeByte(0xFF);

		return buffer.toString();
	}

	public static String channelFrame(Channel channel, String nickname) {
		PacketBuilder buffer = new PacketBuilder(
				SendOpcode.CHANNEL_FRAME.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(channel.getName());
		buffer.writeByte(0x00);
		buffer.writeString(nickname);
		buffer.writeByte(0x00);
		buffer.writeString("6"); // scrollspeed
		buffer.writeByte(0x00);
		buffer.writeString("800"); // width
		buffer.writeByte(0x00);
		buffer.writeString("600"); // height
		buffer.writeByte(0x00);
		buffer.writeString("0"); // position x
		buffer.writeByte(0x00);
		buffer.writeString("0"); // position y
		buffer.writeByte(0x00);
		buffer.writeString(channel.getBackgroundImage());
		buffer.writeByte(0x00);
		buffer.writeString(String.valueOf(channel.getBackgroundPosition()));
		buffer.writeByte(0x00);
		buffer.writeString("-");
		addChannelStyle(buffer, channel.getStyle());
		buffer.writeByte(0x00);
		buffer.writeString("F"); // channel combobox
		buffer.writeByte(0x00);
		buffer.writeString("3000"); // max. message length
		buffer.writeByte(0x00);
		buffer.writeString("F"); // help button
		buffer.writeByte(0x00);
		buffer.writeString("F"); // report button
		buffer.writeByte(0x00);
		buffer.writeString("F"); // feedback button
		buffer.writeByte(0x00);
		buffer.writeString("F"); // search button

		return buffer.toString();
	}

	public static String channelList(Collection<Channel> channels) {
		PacketBuilder buffer = new PacketBuilder(
				SendOpcode.CHANNEL_LIST.getValue());

		for (Channel channel : channels) {
			int count = channel.countClients();
			buffer.writeByte(0x00);
			buffer.writeString(channel.getName());
			buffer.writeByte(0x0A);
			buffer.writeString(String.valueOf(count));
			buffer.writeByte(0x00);
			buffer.writeByte('p'); // font weight
			buffer.writeByte(0x00);
			buffer.writeString("B");

			if (count == channel.getSize()) {
				buffer.writeByte(0x00);
				buffer.writeString("pics/icon_fullChannel.gif");
			}

			buffer.writeByte(0x00);
			buffer.writeString("-");
		}

		return buffer.toString();
	}

	public static String switchChannel(String channelFrom, String channelTo) {
		PacketBuilder buffer = new PacketBuilder(
				SendOpcode.SWITCH_CHANNEL.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(channelFrom);
		buffer.writeByte(0x00);
		buffer.writeString(channelTo);

		return buffer.toString();
	}

	public static String publicMessage(String sender, String channel,
			String message) {
		PacketBuilder buffer = new PacketBuilder(
				SendOpcode.PUBLIC_MESSAGE.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(sender);
		buffer.writeByte(0x00);
		buffer.writeString(channel);
		buffer.writeByte(0x00);
		buffer.writeString(message);

		return buffer.toString();
	}

	public static String updateChannelBackground(Channel channel) {
		PacketBuilder buffer = new PacketBuilder(
				SendOpcode.UPDATE_CHANNEL_BACKGROUND.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(channel.getName());
		buffer.writeByte(0x00);
		buffer.writeString(channel.getBackgroundImage());
		buffer.writeByte(0x00);
		buffer.writeString(String.valueOf(channel.getBackgroundPosition()));

		return buffer.toString();
	}

	public static String addUser(Channel channel, Client client, boolean self) {
		PacketBuilder buffer = new PacketBuilder(SendOpcode.ADD_USER.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(channel.getName());
		buffer.writeByte(0x00);
		buffer.writeString(client.getName());
		buffer.writeByte(0x00);
		buffer.writeByte(self ? 'b' : 'p'); // font weight
		buffer.writeByte(0x00);
		buffer.writeString(channel.getStyle().getRankColor(client));

		for (Pair<String, Integer> icon : client.getIcons()) {
			buffer.writeByte(0x00);
			buffer.writeString(icon.getLeft());
			buffer.writeByte(0x00);
			buffer.writeString(String.valueOf(icon.getRight()));
		}

		buffer.writeByte(0x00);
		buffer.writeString("-"); // login message

		return buffer.toString();
	}

	public static String addIcon(String channel, String nickname,
			Pair<String, Integer> icon) {
		PacketBuilder buffer = new PacketBuilder(SendOpcode.ADD_ICON.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(channel);
		buffer.writeByte(0x00);
		buffer.writeString(nickname);
		buffer.writeByte(0x00);
		buffer.writeString(icon.getLeft());
		buffer.writeByte(0x00);
		buffer.writeString(String.valueOf(icon.getRight()));

		return buffer.toString();
	}

	public static String playSound(String file) {
		PacketBuilder buffer = new PacketBuilder(
				SendOpcode.PLAY_SOUND.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(file);

		return buffer.toString();
	}

	public static String privateMessage(String sender, String recipient,
			String channelTo, String message, String channelFrom) {
		PacketBuilder buffer = new PacketBuilder(
				SendOpcode.PRIVATE_MESSAGE.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(sender);
		buffer.writeByte(0x00);
		buffer.writeString(recipient);
		buffer.writeByte(0x00);
		buffer.writeString(channelTo);
		buffer.writeByte(0x00);
		buffer.writeString(message);
		buffer.writeByte(0x00);
		buffer.writeString(channelFrom);

		return buffer.toString();
	}

	public static String action(String sender, String channel, String message) {
		PacketBuilder buffer = new PacketBuilder(SendOpcode.ACTION.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(sender);
		buffer.writeByte(0x00);
		buffer.writeString(channel);
		buffer.writeByte(0x00);
		buffer.writeString(message);

		return buffer.toString();
	}

	public static String userList(Channel channel) {
		PacketBuilder buffer = new PacketBuilder(
				SendOpcode.USER_LIST.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(channel.getName());

		for (Client client : channel.getClients()) {
			buffer.writeByte(0x00);
			buffer.writeString(client.getName());
			buffer.writeByte(0x00);
			buffer.writeByte('p'); // font weight
			buffer.writeByte(0x00);
			buffer.writeString(channel.getStyle().getRankColor(client));

			for (Pair<String, Integer> icon : client.getIcons()) {
				buffer.writeByte(0x00);
				buffer.writeString(icon.getLeft());
				buffer.writeByte(0x00);
				buffer.writeString(String.valueOf(icon.getRight()));
			}

			buffer.writeByte(0x00);
			buffer.writeString("-");
		}

		return buffer.toString();
	}

	public static String removeUser(String name, String channel) {
		PacketBuilder buffer = new PacketBuilder(
				SendOpcode.REMOVE_USER.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(name);
		buffer.writeByte(0x00);
		buffer.writeString(channel);

		return buffer.toString();
	}

	public static String openURL(String url, String target) {
		PacketBuilder buffer = new PacketBuilder(SendOpcode.OPEN_URL.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(url);
		buffer.writeByte(0x00);
		buffer.writeString(target);

		return buffer.toString();
	}

	public static String removeIcon(String channel, String nickname, String icon) {
		PacketBuilder buffer = new PacketBuilder(
				SendOpcode.REMOVE_ICON.getValue());

		buffer.writeByte(0x00);
		buffer.writeString(channel);
		buffer.writeByte(0x00);
		buffer.writeString(nickname);
		buffer.writeByte(0x00);
		buffer.writeString(icon);

		return buffer.toString();
	}
}
