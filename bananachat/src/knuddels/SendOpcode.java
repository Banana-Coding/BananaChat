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
public enum SendOpcode {
    PLAY_MP3("#"),
    HELLO("("),
    PONG(","),
    UPDATE_CHANNEL_SETTINGS("1"),
    BUTLER("5"),
    KICK("6"),
    MODULE(":"),
    CHANNEL_FRAME("a"),
    CHANNEL_LIST("b"),
    SWITCH_CHANNEL("d"),
    PUBLIC_MESSAGE("e"),
    UPDATE_CHANNEL_BACKGROUND("j"),
    POPUP("k"),
    ADD_USER("l"),
    ADD_ICON("m"),
    PLAY_SOUND("o"),
    PRIVATE_MESSAGE("r"),
    ACTION("t"),
    USER_LIST("u"),
    REMOVE_USER("w"),
    OPEN_URL("x"),
    REMOVE_ICON("z");

    private String opcode;

    private SendOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getValue() {
        return opcode;
    }
}
