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

/**
 *
 * @author Flav
 */
public enum ReceiveOpcode {
    EXCEPTION("1"),
    DISCONNECT("d"),
    CHAT("e"),
    PING("h"),
    POLL("iv"),
    LINK_CLICKED("j"),
    JOIN_CHANNEL("n"),
    Q_TOKEN("q"),
    REQUEST_USER_LIST("r"),
    HANDSHAKE("t"),
    REQUEST_HELP("u"),
    LEAVE_CHANNEL("w"),
    WHOIS("whois");

    private String opcode;

    private ReceiveOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getValue() {
        return opcode;
    }
}
