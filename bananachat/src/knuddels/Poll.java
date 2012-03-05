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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import tools.KCodeParser;
import tools.popup.Button;
import tools.popup.Panel;
import tools.popup.Popup;

/**
 *
 * @author Flav
 */
public class Poll {
    private final static Map<Integer, Poll> polls;
    private static int runningId;
    private final Map<String, String> votes;
    private boolean closed;
    private String question;
    private List<String> answers;
    private int id;

    static {
        polls = new HashMap<Integer, Poll>();
    }

    public Poll(String question, List<String> answers) {
        votes = new HashMap<String, String>();
        closed = false;
        this.question = question;
        this.answers = answers;
    }

    public static Poll get(int id) {
        synchronized (polls) {
            return polls.get(id);
        }
    }

    public void create() {
        synchronized (polls) {
            runningId++;
            id = runningId;
            polls.put(id, this);
        }

        Popup popup = new Popup("Umfrage", String.format("Umfrage #%s", id), String.format("%s§##Unter allen Teilnehmern werden 5 Knuddels verlost.", question), 400, 300);
        Panel panel = new Panel();

        for (String answer : answers) {
            Button button = new Button(answer);
            button.enableAction();
            panel.addComponent(button);
        }

        popup.addPanel(panel);
        popup.setOpcode(ReceiveOpcode.POLL.getValue(), String.valueOf(id));
        String poll = popup.toString();

        for (Client client : Server.get().getClients()) {
            client.send(poll);
        }

        new Timer().schedule(new TimerTask() {
            public void run() {
                closed = true;

                if (votes.isEmpty()) {
                    return;
                }

                Object[] participants = votes.keySet().toArray();
                String winner = (String) participants[new Random().nextInt(participants.length)];
                Client client = Server.get().getClient(winner);

                if (client != null) {
                    client.setKnuddels(client.getKnuddels() + 5);
                }

                StringBuilder result = new StringBuilder();
                result.append(question);
                result.append("§#");

                for (String answer : answers) {
                    int count = 0;

                    for (String a : votes.values()) {
                        if (a.equals(answer)) {
                            count++;
                        }
                    }

                    result.append("#_");
                    result.append(KCodeParser.escape(answer));
                    result.append(":_ ");
                    result.append(count);
                }

                result.append(String.format("##...und gewonnen hat _°B>_h%s|/serverpp \"|/w \"<r°_!", winner));
                String popup = Popup.create("Umfrage", String.format("Umfrage #%s - Ergebnis", id), result.toString(), 400, 300);

                for (Client c : Server.get().getClients()) {
                    c.send(popup);
                }
            }
        }, 60 * 1000);
    }

    public void vote(Client client, String answer) {
        synchronized (votes) {
            if (!votes.containsKey(client.getName()) && answers.contains(answer)) {
                if (closed) {
                    String popup = Popup.create("Umfrage", String.format("Umfrage #%s - Problem", id), "Du hast dir für die Beantwortung der Umfrage zu lange Zeit gelassen.", 400, 300);
                    client.send(popup);
                } else {
                    votes.put(client.getName(), answer);
                }
            }
        }
    }
}
