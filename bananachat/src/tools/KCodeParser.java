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

package tools;

/**
 *
 * @author Flav
 */
public class KCodeParser {
    public static String parse(String str, boolean filter, int maxLineBreaks, int minSize, int maxSize) {
        StringBuilder ret = new StringBuilder();
        StringBuilder code = null;
        boolean isCode = false;
        boolean escape = false;
        byte lineBreaks = 0;

        for (int i = 0; i < str.length(); i++) {
            char current = str.charAt(i);

            if (current == '°' && !escape) {
                isCode = !isCode;

                if (isCode) {
                    if (str.lastIndexOf('°') == i) {
                        break;
                    }

                    code = new StringBuilder();
                } else {
                    if (filter && !code.toString().isEmpty()) {
                        StringBuilder filtered = new StringBuilder();
                        StringBuilder rgb = null;
                        boolean isRGB = false;
                        String size;

                        for (int j = 0; j < code.length(); j++) {
                            char c = code.charAt(j);

                            if (isRGB) {
                                if (c == ']') {
                                    if (!validateRGB(rgb.toString().split(","))) {
                                        break;
                                    }

                                    isRGB = false;
                                    filtered.append('[');
                                    filtered.append(rgb);
                                    filtered.append(']');
                                } else if (isNumber(c) || c == ',') {
                                    rgb.append(c);
                                } else {
                                    break;
                                }
                            } else {
                                if (isNumber(c)) {
                                    size = "";

                                    while (j < code.length()) {
                                        c = code.charAt(j);

                                        if (!isNumber(c)) {
                                            j--;
                                            break;
                                        }

                                        size += c;
                                        j++;
                                    }

                                    if (Integer.parseInt(size) < minSize) {
                                        size = String.valueOf(minSize);
                                    } else if (Integer.parseInt(size) > maxSize) {
                                        size = String.valueOf(maxSize);
                                    }

                                    filtered.append(size);
                                } else if (isColor(c) || c == 'r') {
                                    filtered.append(c);
                                } else if (c == '[') {
                                    isRGB = true;
                                    rgb = new StringBuilder();
                                } else {
                                    break;
                                }
                            }
                        }

                        if (filtered.toString().isEmpty()) {
                            continue;
                        }

                        code = filtered;
                    }

                    ret.append('°');
                    ret.append(code);
                    ret.append('°');
                }

                continue;
            }

            if (isCode) {
                code.append(current);
            } else {
                if (current == '#' && !escape) {
                    if (!filter || lineBreaks < maxLineBreaks) {
                        ret.append("#°!°");
                        lineBreaks++;
                    } else {
                        ret.append(' ');
                    }
                } else {
                    ret.append(current);
                }
            }

            if (current == '\\') {
                escape = !escape;
            } else {
                escape = false;
            }
        }

        return ret.toString().trim();
    }

    public static String escape(String message) {
        return message
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("#", "\\#")
                .replace("_", "\\_")
                .replace("§", "\\§")
                .replace("°", "\\°")
                .trim();
    }

    private static boolean isNumber(int character) {
        return character >= '0' && character <= '9';
    }

    private static boolean isColor(int character) {
        switch (character) {
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'G':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'R':
            case 'W':
            case 'Y':
                return true;
            default:
                return false;
        }
    }

    private static boolean validateRGB(String[] rgb) {
        if (rgb.length < 3) {
            return false;
        }

        for (String color : rgb) {
            if (color.isEmpty() || color.length() > 3) {
                return false;
            }

            short value = Short.parseShort(color);

            if (value < 0 || value > 255) {
                return false;
            }
        }

        return true;
    }
}
