/* Banana-Chat - The first Open Source Knuddels Emulator
 * Copyright (C) 2011-2013  Flav <http://banana-coding.com>
 *
 * Diese Datei unterliegt dem Copyright von Banana-Coding und
 * darf verŠndert, aber weder in andere Projekte eingefŸgt noch
 * reproduziert werden.
 *
 * Der Emulator dient - sofern der Client nicht aus Eigenproduktion
 * stammt - nur zu Lernzwecken, das Hosten des originalen Clients
 * ist untersagt und wird der Knuddels GmbH gemeldet.
 */

package knuddels;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import tools.KCodeParser;
import tools.PacketCreator;
import tools.popup.Button;
import tools.popup.Label;
import tools.popup.Panel;
import tools.popup.Popup;
import tools.popup.TextField;

/**
 * 
 * @author Flav
 * @since 1.0
 */
public class CommandParser {
	private static NumberFormat nf;

	static {
		nf = NumberFormat.getInstance(Locale.GERMAN);
	}

	public static void parse(String message, Client client, Channel channel) {
		String command = KCodeParser.escape(message.substring(1).split(" ")[0]);
		String cmd = command.toLowerCase();
		String arg = "";

		if (message.length() > cmd.length() + 1) {
			arg = message.substring(message.indexOf(' ') + 1);
		}

		if (cmd.equals("a")) {
			channel.broadcastMessage(arg, client, false);
		} else if (cmd.equals("afk") || cmd.equals("away")) {
			if (channel.getGame() == null) {
				String msg = "verschwindet mal ganz kurz.";
				arg = KCodeParser.parse(arg, !client.isModerator(), 0, 10, 20);
				arg = Server.get().parseSmileys(arg);

				if (!arg.isEmpty()) {
					msg = String.format("%s (%s§)", msg, arg);
				}

				channel.broadcastAction(client.getName(), msg);
			}

			client.setAway(true);
		} else if (cmd.equals("cc") || cmd.equals("go")) {
			String ch = KCodeParser.escape(arg);
			boolean change = true;

			if (!ch.isEmpty() && arg.charAt(0) == '+') {
				change = false;
				ch = ch.substring(1).trim();
			}

			if (ch.isEmpty()) {
				return;
			}

			Channel channelTo = Server.get().getChannel(ch);

			if (channelTo == null) {
				client.sendButlerMessage(channel.getName(),
						String.format("Der Channel _%s existiert nicht_.", ch));
			} else if (channelTo.getClients().contains(client)) {
				client.sendButlerMessage(channel.getName(),
						String.format("Du bist doch schon im Channel %s.", ch));
			} else if (!client.isModerator()
					&& channelTo.countClients() == channelTo.getSize()) {
				client.sendButlerMessage(
						channel.getName(),
						String.format(
								"Dieser Channel ist auf _maximal %s_ Leute beschränkt, bitte wähl einen anderen Channel.",
								channelTo.getSize()));
			} else {
				client.joinChannel(channelTo);

				if (change) {
					channel.leave(client);
					client.leaveChannel(channel);
					client.send(PacketCreator.switchChannel(channel.getName(),
							channelTo.getName()));
					client.send(PacketCreator.updateChannelSettings(channelTo));
					client.send(PacketCreator
							.updateChannelBackground(channelTo));
				} else {
					client.send(PacketCreator.channelFrame(channelTo,
							client.getName()));
				}

				channelTo.join(client);
			}
		} else if (cmd.equals("edit")) {
		} else if (cmd.equals("f")) {
		} else if (cmd.equals("forum")) {
			client.send(PacketCreator.openURL(
					"http://forum.knuddels.de/categories.html", "forum"));
		} else if (cmd.equals("h") || cmd.equals("help") || cmd.equals("hilfe")) {
		} else if (cmd.equals("ig")) {
		} else if (cmd.equals("info")) {
		} else if (cmd.equals("kick") && client.isModerator()) {
			String nickname = KCodeParser.escape(arg);

			if (nickname.isEmpty()) {
				return;
			}

			if (nickname.equalsIgnoreCase(client.getName())) {
				client.sendButlerMessage(channel.getName(),
						"Sie können sich nicht selbst hinauswerfen.");
				return;
			}

			Client target = Server.get().getClient(arg);

			if (target == null) {
				client.sendButlerMessage(channel.getName(),
						String.format("%s ist mir unbekannt.", nickname));
			} else if (target.getRank() >= client.getRank()) {
				client.sendButlerMessage(
						channel.getName(),
						String.format(
								"Sie haben nicht genügend Rechte um %s hinauszuwerfen.",
								target.getName()));
			} else {
				target.logout("Hinausgeworfen");
			}
		} else if (cmd.equals("kiss")) {
			String nickname = KCodeParser.escape(arg);

			if (nickname.isEmpty()) {
				return;
			}

			if (nickname.equalsIgnoreCase(client.getName())) {
				client.sendButlerMessage(channel.getName(),
						"Du kannst Dich doch nicht selbst küssen!");
				return;
			}

			Client target = Server.get().getClient(arg);

			if (target == null) {
				client.sendButlerMessage(channel.getName(),
						String.format("%s ist mir unbekannt.", nickname));
			} else if (!channel.getClients().contains(target)) {
				client.sendButlerMessage(
						channel.getName(),
						String.format(
								"°>_h%s|/serverpp \"|/w \"<° hält sich im Moment _in einem anderen Channel_ auf.",
								target.getName()));
			} else {
				target.increaseKisses();
				channel.broadcastAction(
						">",
						String.format(
								"°>_h%s|/serverpp \"|/w \"<° kann einfach nicht anders und gibt °>_h%s|/serverpp \"|/w \"<° einen langen Kuss.",
								client.getName(), target.getName()));
			}
		} else if (cmd.equals("knuddel")) {
			String nickname = KCodeParser.escape(arg);

			if (nickname.isEmpty()) {
				return;
			}

			if (nickname.equalsIgnoreCase(client.getName())) {
				client.sendButlerMessage(channel.getName(),
						"Du kannst dich doch nicht selbst knuddeln!");
				return;
			}

			Client target = Server.get().getClient(arg);

			if (target == null) {
				client.sendButlerMessage(channel.getName(),
						String.format("%s ist mir unbekannt.", nickname));
			} else if (!channel.getClients().contains(target)) {
				client.sendButlerMessage(
						channel.getName(),
						String.format(
								"°>_h%s|/serverpp \"|/w \"<° hält sich im Moment _in einem anderen Channel_ auf.",
								target.getName()));
			} else if (client.getKnuddels() < 1) {
				client.sendButlerMessage(channel.getName(),
						"Knuddeln kann man nur, wenn man auch Knuddels hat, und die fehlen dir leider.");
			} else {
				client.setKnuddels(client.getKnuddels() - 1);
				target.setKnuddels(target.getKnuddels() + 1);
				channel.broadcastAction(
						">",
						String.format(
								"°>_h%s|/serverpp \"|/w \"<° hat °>_h%s|/serverpp \"|/w \"<° scheinbar richtig gern und °RR°knuddelt°BB° eifrig drauf los.",
								client.getName(), target.getName()));
			}
		} else if (cmd.equals("m")) {
		} else if (cmd.equals("me")) {
			arg = KCodeParser.parse(arg, !client.isModerator(), 5, 10, 20);
			arg = Server.get().parseSmileys(arg);

			if (!arg.isEmpty()) {
				channel.broadcastAction(client.getName(), arg);
			}
		} else if (cmd.equals("p")) {
			int split = arg.indexOf(':');

			if (split < 1 || split == arg.length() - 1) {
				return;
			}

			String recipient = arg.substring(0, split);
			String[] recipients = recipient.split(",");

			if (recipients.length > 5 && !client.isModerator()) {
				return;
			}

			List<Client> targets = new ArrayList<Client>();

			for (String rcp : recipients) {
				rcp = KCodeParser.escape(rcp);

				if (rcp.isEmpty()) {
					continue;
				}

				Client target = Server.get().getClient(rcp);

				if (target == null) {
					client.sendButlerMessage(channel.getName(),
							String.format("%s ist mir unbekannt.", rcp));
				} else if (!targets.contains(target)) {
					targets.add(target);
				}
			}

			if (targets.isEmpty()) {
				return;
			}

			String msg = arg.substring(split + 1);
			client.sendPrivateMessage(targets, msg, channel, false);
		} else if (cmd.equals("poll") && client.isAdministrator()) {
			Popup popup = new Popup(
					"Umfrage",
					"Umfrage erstellen",
					"Gib unten eine Frage und mindestens zwei verschiedene Antwortmöglichkeiten - durch Semikolons getrennt - ein. Jeder Chatter hat dann die Möglichkeit die Umfrage innerhalb von einer Minute zu beantworten und dabei 5 Knuddels abzusahnen, welche unter allen Teilnehmern verlost werden.",
					400, 300);

			Panel panel1 = new Panel();
			panel1.addComponent(new Label("Frage:"));
			panel1.addComponent(new TextField(15));
			panel1.addComponent(new Label("Antworten:"));
			panel1.addComponent(new TextField(15));
			popup.addPanel(panel1);

			Panel panel2 = new Panel();
			Button button = new Button("Erstellen");
			button.setStyled(true);
			button.enableAction();
			panel2.addComponent(button);
			popup.addPanel(panel2);

			popup.setOpcode(ReceiveOpcode.POLL.getValue(), "new");
			client.send(popup.toString());
		} else if (cmd.equals("serverpp")) {
			String nickname = KCodeParser.escape(arg);

			if (nickname.isEmpty()) {
				return;
			}

			Client target = Server.get().getClient(arg);

			if (target == null) {
				client.sendButlerMessage(channel.getName(),
						String.format("%s ist mir unbekannt.", nickname));
			} else {
				client.send(PacketCreator.privateChat(channel.getName(),
						target.getName()));
			}
		} else if (cmd.equals("shutdown") && client.isAdministrator()) {
			for (Object target : Server.get().getClients().toArray()) {
				((Client) target).disconnect();
			}

			System.exit(0);
		} else if (cmd.equals("top")) {
		} else if (cmd.equals("topic") && client.isModerator()) {
			if (arg.isEmpty()) {
				channel.setTopic(null);
				client.sendButlerMessage(channel.getName(),
						"Das Thema wurde gelöscht.");
			} else {
				channel.setTopic(arg);
			}
		} else if (cmd.equals("w") || cmd.equals("whois")) {
			String nickname = KCodeParser.escape(arg);
			Client target;
			boolean online = true;

			if (nickname.isEmpty()
					|| nickname.equalsIgnoreCase(client.getName())) {
				target = client;
			} else {
				target = Server.get().getClient(nickname);

				if (target == null) {
					online = false;
					target = new Client(null);
					target.loadStats(nickname);

					if (target.getName() == null) {
						client.sendButlerMessage(channel.getName(), String
								.format("%s ist mir unbekannt.", nickname));
						return;
					}
				}
			}

			nickname = target.getName();
			String title = String.format("Who is %s ?", nickname);
			StringBuilder whois = new StringBuilder();

			whois.append(String.format("_°>_h%s|/serverpp \"<r°_", nickname));
			whois.append(String.format(" hat sich am _%s_",
					target.getRegistrationDate()));
			whois.append(String.format(" um %s", target.getRegistrationTime()));
			whois.append(String
					.format(" bei Knuddels registriert und seitdem schon _%s_ Minuten hier verbracht.##",
							nf.format(target.getOnlineTime() / 60)));

			if (online) {
				Channel ch = target.getChannel();

				if (ch == null) {
					whois.append(String
							.format("°>py_g.gif<°°%%05°%s ist im Moment im Channel _? °E°ONLINE°r°_!°%%00°##",
									nickname));
				} else {
					whois.append(String
							.format("°>py_g.gif<°°%%05°%s ist im Moment im Channel _°>_h%s|/go \"|/go +\"<° °E°ONLINE°r°_!°%%00°##",
									nickname, ch.getName()));
				}
			} else {
				whois.append(String
						.format("°>py_r.gif<°°%%05°%s ist im Moment _OFFLINE_!°%%00°##",
								nickname));
			}

			whois.append(String.format("%s hat...°%%35°", nickname));
			whois.append(String.format("°>gt.gif<° _°R°%s°r°_ %s...#", nf
					.format(target.getKisses()),
					target.getKisses() == 1 ? "Knutschfleck" : "Knutschflecken"));
			whois.append(String.format(
					"°>gt.gif<° und kann noch _°R°%sx°r° knuddeln_!",
					nf.format(target.getKnuddels())));
			whois.append("°%00°##°-°");

			if (target.getGender() == 1) {
				whois.append("_Geschlecht_:°%35°männl. °>male.png<°°%00°#");
			} else if (target.getGender() == 2) {
				whois.append("_Geschlecht_:°%35°weibl. °>female.png<°°%00°#");
			}

			if (client.isModerator()) {
				whois.append("°-°");
				whois.append(String.format("_IP-Adresse_:°%%35°%s°%%00°#",
						target.getIPAddress()));
			}

			Popup popup = new Popup(title, title, whois.toString(), 460, 350);
			Panel panel = new Panel();
			panel.addComponent(new Button("   OK   "));

			if (target != client) {
				Button buttonMessage = new Button("Nachricht");
				buttonMessage.setCommand(String
						.format("/serverpp %s", nickname));
				buttonMessage.disableClose();
				panel.addComponent(buttonMessage);
			}

			popup.addPanel(panel);
			popup.setOpcode(ReceiveOpcode.WHOIS.getValue(), nickname);
			client.send(popup.toString());
		} else {
			client.sendButlerMessage(channel.getName(), String.format(
					"Die Funktion /%s gibt's hier leider nicht.", command));
		}
	}
}
