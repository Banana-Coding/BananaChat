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

package knuddels.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import knuddels.Channel;
import knuddels.Client;
import knuddels.Server;
import tools.popup.Popup;

/**
 *
 * @author Flav
 */
public class Mafia implements Game, Runnable {
    private final Map<String, MafiaPlayer> players;
    private Channel channel;
    private Thread thread;
    private byte status;

    public Mafia(Channel channel) {
        players = new HashMap<String, MafiaPlayer>();
        this.channel = channel;
        init();
    }

    private void init() {
        players.clear();
        thread = new Thread(this);
        status = 0;
    }

    private void action() {
        if (status == 1 || status == 2) {
            for (int i = 0; i < 20; i++) {
                if (status == 3) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }

        if (status == 1) {
            synchronized (players) {
                if (players.size() < 5) {
                    channel.broadcastButlerMessage(String.format("Noch können wir leider nicht anfangen. Wir haben _erst %s Spieler_, brauchen aber _mindestens 5 Spieler_. Wer kein Angsthase ist, meldet sich bei mir °R°privat°°.", players.size()));
                    status++;
                } else {
                    status = 3;
                }
            }

            action();
        } else if (status == 2) {
            synchronized (players) {
                if (players.size() < 5) {
                    channel.broadcastButlerMessage(String.format("Leider findet unsere Mafiarunde nicht statt. Mindestens _5 Spieler benötigen_ wir, es haben sich aber _nur %s Spieler_ gemeldet.", players.size()));
                    status = 0;
                } else {
                    status++;
                    action();
                }
            }
        } else if (status == 3) {
            // rollenverteilung
        }
    }

    public boolean parsePublicMessage(String message, Client client) {
        if (status > 2 && !client.isModerator()) {
            client.sendButlerMessage(channel.getName(), "In diesem Channel kannst du nicht öffentlich sprechen.");
            return false;
        }

        String msg = message.toLowerCase();

        if (msg.contains(Server.get().getButler().getName().toLowerCase()) && msg.contains("mafia")) {
            channel.broadcastMessage(message, client, true);

            if (status == 0) {
                status++;
                thread.start();
                String popup = Popup.create("Start Mafiarunde", "Start Mafiarunde", String.format("##Im Channel %s beginnt nun die _nächste Mafiarunde_.##_°B>_cKlicken Sie hier|/p James:ok<r°_ um daran teilzunehmen.", channel.getName()), 400, 250);

                for (Client c : channel.getClients()) {
                    c.send(popup);
                }

                channel.broadcastButlerMessage("Eine _neue Runde Mafia_ beginnt. Wer bei diesem tödlichen Spiel mitspielen möchte, möge mir dies °R°privat °°mitteilen.");
            } else {
                channel.broadcastButlerMessage("Mafia läuft bereits.");
            }

            return false;
        }

        return true;
    }

    public boolean parsePrivateMessage(List<Client> targets, String message, Client client) {
        if (status > 2 && !client.isModerator()) {
            client.sendButlerMessage(channel.getName(), "Diese Funktion steht dir hier nicht zur Verfügung.");
            return false;
        }

        if (targets.contains(Server.get().getButler())) {
            if (status == 1 || status == 2) {
                client.sendPrivateMessage(targets, message, channel, true);

                synchronized (players) {
                    if (players.containsKey(client.getName())) {
                        client.sendButlerMessage(channel.getName(), "Sie sind bereits angemeldet.");
                    } else {
                        players.put(client.getName(), new MafiaPlayer(client.getName()));
                        client.sendButlerMessage(channel.getName(), "Sie sind nun für die neue Mafiarunde angemeldet.");

                        if (players.size() == channel.getSize() - 1) {
                            status = 3;
                        }
                    }
                }

                return false;
            }
        }

        return true;
    }

    public void onLeave(Client client) {
        synchronized (players) {
            if (players.containsKey(client.getName())) {
                MafiaCharacter character = players.get(client.getName()).getCharacter();

                if (character != null) {
                    // spiel endet möglicherwiese hier
                }

                players.remove(client.getName());
            }
        }
    }

    public void run() {
        action();
        init();
    }
}
