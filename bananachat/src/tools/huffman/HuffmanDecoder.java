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

package tools.huffman;
import java.util.Hashtable;

/**
 * 
 * @author Knuddels GmbH & Co. KG
 * @since 1.1
 */

@SuppressWarnings({ "unused" })
public class HuffmanDecoder {
	private Object a;
	private Object b[];
	private long c;
	private long d;
	private boolean e;
	private byte f[];
	private int g;
	private int h;

	public HuffmanDecoder(String tree) {
		g = 0;
		h = 0;
		c = 0L;
		d = 0L;
		b = new Object[2];
		a(b, tree);
	}

	private final void a(Object aobj[], String s) {
		int i = 0;
		int j = s.length();
		int k = 1;
		int l = -33;
		boolean flag = false;
		boolean flag1 = false;
		int j1;

		for (; i < j; i += j1 + 1) {
			char c1 = s.charAt(i);
			int i1;

			if (c1 == '\377') {
				j1 = s.charAt(i + 1) + 1;
				i1 = s.charAt(i + 2);
				i += 2;
			} else {
				j1 = c1 / 21 + 1;
				i1 = c1 % 21;
			}

			if ((k & 1) == 0) {
				k++;

				for (; l < i1; l++) {
					k <<= 1;
				}
			} else {
				while ((k & 1) == 1) {
					k >>= 1;
					l--;
				}

				k++;

				for (; l < i1; l++) {
					k <<= 1;
				}
			}

			int k1 = a(k, i1);
			String s1 = s.substring(i + 1, i + j1 + 1);

			if (j1 == 3 && s1.equals("\\\\\\")) {
				a = s1;
			}

			a(aobj, s1, k1, i1);
		}
	}

	public synchronized String decode(byte[] buffer) {
		if (buffer == null) {
			return null;
		}

		StringBuffer stringbuffer = new StringBuffer((buffer.length * 100) / 60);
		f = buffer;
		g = 0;
		h = 0;
		e = false;
		Object aobj[] = b;

		do {
			if (e) {
				break;
			}

			aobj = (Object[]) aobj[a()];

			if (aobj[0] == null) {
				if (aobj[1] == a) {
					int i = 0;

					for (int j = 0; j < 16; j++) {
						i += a() << j;
					}

					stringbuffer.append((char) i);
				} else {
					stringbuffer.append((String) aobj[1]);
				}

				aobj = b;
			}
		} while (true);

		String s = stringbuffer.toString();
		d += s.length();
		c += buffer.length;

		return s;
	}

	private final boolean a(Object aobj[], String s, int i, int j) {
		if (j == 0) {
			aobj[1] = s;
			return aobj[0] == null;
		}

		if (aobj[0] == null) {
			if (aobj[1] != null) {
				return false;
			}

			aobj[0] = ((Object) (new Object[2]));
			aobj[1] = ((Object) (new Object[2]));
		}

		return a((Object[]) aobj[i & 1], s, i >> 1, j - 1);
	}

	private final int a() {
		int i = 0;

		if ((f[h] & 1 << g) != 0) {
			i = 1;
		}

		g++;

		if (g > 7) {
			g = 0;
			h++;
			e = h == f.length;
		}

		return i;
	}

	private final int a(int i, int j) {
		int k = 0;
		int l = 1;
		int i1 = 1 << j - 1;

		for (; j > 0; j--) {
			if ((i & l) != 0) {
				k += i1;
			}

			l <<= 1;
			i1 >>= 1;
		}

		return k;
	}
}