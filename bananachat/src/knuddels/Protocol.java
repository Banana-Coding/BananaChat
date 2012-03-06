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

import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Flav
 */
public class Protocol {
	public static String decode(InputStream in) throws IOException {
		byte first = (byte) in.read();

		if (first == -1) {
			throw new IOException("End of stream");
		}

		int length;

		if (first >= 0) {
			length = first + 1;
		} else {
			length = (first & 0x1F) + 1;
			int count = (first & 0x60) >>> 5;

			for (int i = 0; i < count; i++) {
				length += in.read() << (i << 3) + 5;
			}
		}

		byte[] buffer = new byte[length];
		in.read(buffer, 0, length);
		return Huffman.decode(buffer);
	}

	public static byte[] encode(byte[] message) {
		try {
			return Huffman.encode(new String(message, "UTF8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
