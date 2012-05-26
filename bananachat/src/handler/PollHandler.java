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

package handler;

import java.util.ArrayList;
import java.util.List;
import knuddels.Client;
import knuddels.Poll;
import tools.popup.Popup;

/**
 * 
 * @author Flav
 */
public class PollHandler {
	public static void handle(String[] tokens, Client client) {
		if (tokens[1].equals("new") && client.isAdministrator()) {
			String question = tokens[3].trim();

			List<String> answers = new ArrayList<String>();

			for (String answer : tokens[4].split(";")) {
				answer = answer.trim();

				if (!answer.isEmpty()) {
					boolean contains = false;

					for (String a : answers) {
						if (a.equalsIgnoreCase(answer)) {
							contains = true;
							break;
						}
					}

					if (contains) {
						continue;
					}

					answers.add(answer);
				}
			}

			if (question.isEmpty() || answers.size() < 2) {
				String popup = Popup
						.create("Umfrage",
								"Problem",
								"Es müssen eine Frage und mindestens zwei verschiedene Antwortmöglichkeiten gegeben sein. Zweimal die selbe Antwort ist nicht möglich.",
								400, 300);
				client.send(popup);
			} else {
				new Poll(question, answers).create();
			}
		} else {
			Poll poll = Poll.get(Integer.parseInt(tokens[1]));

			if (poll != null) {
				poll.vote(client, tokens[2]);
			}
		}
	}
}
