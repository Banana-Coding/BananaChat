/* Banana-Chat - The first Open Source Knuddels Emulator
 *
 * Diese Datei wurde von Flav aus dem Chatapplet von Knuddels.de extrahiert und
 * unterliegt damit dem Copyright der Knuddels GmbH & Co. KG. 
 * Die Nutzung geschieht daher auf eigene Gefahr.
 * 
 * Zu finden auf: http://banana-coding.com/board7-inoffizielle-releases/390-huffman/
 */

package tools.huffman;

import java.util.Hashtable;

/**
 * 
 * @author Knuddels GmbH & Co. KG
 */

@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class HuffmanEncoder {
	private int a;
	private int b;
	private Hashtable c;
	private long d;
	private long e;
	private byte f[];
	private int g;
	private int h;
	private Character i[];
	private Short j[];

	public HuffmanEncoder(String tree) {
		f = new byte[65535];
		g = 0;
		h = 0;
		d = 0L;
		e = 0L;
		c = new Hashtable(1, 1.0F);
		a();
		a(tree);
	}

	private final void a(int k) {
		int l = k >> 24;
		int i1 = k - (l << 24);

		if (g != 0 && l > 0) {
			l += g - 8;
			i1 <<= g;
			f[h++] += (byte) i1;
			i1 >>= 8;
			g = 0;
		}

		for (; l > 0; l -= 8) {
			f[h++] = (byte) i1;
			i1 >>= 8;
		}

		if (l < 0) {
			h--;
			g = l + 8;
		}
	}

	private final int a(int k, int l) {
		int i1 = 0;
		int j1 = 1;
		int k1 = 1 << l - 1;

		for (; l > 0; l--) {
			if ((k & j1) != 0) {
				i1 += k1;
			}

			j1 <<= 1;
			k1 >>= 1;
		}

		return i1;
	}

	public synchronized byte[] encode(String str, int size) {
		byte abyte0[] = null;

		if (size > 0) {
			abyte0 = f;
			f = new byte[size];
		}

		g = 0;
		h = 0;
		int l = str.length();
		e += l;

		for (int j1 = 0; j1 < l;) {
			Integer integer = null;
			int i1 = j1 + 1;
			Hashtable hashtable = c;

			do {
				if (j1 >= l) {
					break;
				}

				char c1 = str.charAt(j1);
				Integer integer1 = (Integer) hashtable.get(b(c1));

				if (integer1 != null) {
					i1 = j1 + 1;
					integer = integer1;
				}

				hashtable = (Hashtable) hashtable.get(c(c1));

				if (hashtable == null) {
					break;
				}

				j1++;
			} while (true);

			j1 = i1;

			if (integer == null) {
				a(a);
				a(0x10000000 + str.charAt(j1 - 1));
			} else {
				a(integer.intValue());
			}
		}

		b();
		byte abyte1[] = new byte[h];
		System.arraycopy(f, 0, abyte1, 0, h);
		d += h;

		if (size > 0) {
			f = abyte0;
		}

		return abyte1;
	}

	private final void a(String s) {
		int k = 0;
		int l = s.length();
		int i1 = 1;
		int j1 = -33;
		boolean flag = false;
		boolean flag1 = false;
		int l1;

		for (; k < l; k += l1 + 1) {
			char c1 = s.charAt(k);
			int k1;

			if (c1 == '\377') {
				l1 = s.charAt(k + 1) + 1;
				k1 = s.charAt(k + 2);
				k += 2;
			} else {
				l1 = c1 / 21 + 1;
				k1 = c1 % 21;
			}

			if ((i1 & 1) == 0) {
				i1++;

				for (; j1 < k1; j1++) {
					i1 <<= 1;
				}
			} else {
				while ((i1 & 1) == 1) {
					i1 >>= 1;
					j1--;
				}

				i1++;

				for (; j1 < k1; j1++) {
					i1 <<= 1;
				}
			}

			Integer integer = new Integer(a(i1, k1) + (k1 << 24));
			String s1 = s.substring(k + 1, k + l1 + 1);

			if (b == 0 && k1 > 8) {
				b = a(i1 >> k1 - 8, 8) + 0x8000000;
			}

			if (l1 == 3 && s1.equals("\\\\\\")) {
				a = integer.intValue();
			} else {
				a(s1, integer);
			}
		}
	}

	private final void a() {
		i = new Character[256];
		j = new Short[256];

		for (int k = 0; k < i.length; k++) {
			i[k] = new Character((char) k);
			j[k] = new Short((short) k);
		}

	}

	private final void a(String s, Object obj) {
		a(c, s, 0, obj);
	}

	private final void a(Hashtable hashtable, String s, int k, Object obj) {
		char c1 = s.charAt(k);

		if (k + 1 >= s.length()) {
			if (hashtable.get(b(c1)) != null) {
				throw new RuntimeException((new StringBuilder())
						.append("ERROR while constructing tree ").append(s)
						.toString());
			}

			hashtable.put(b(c1), obj);
		} else {
			Hashtable hashtable1 = (Hashtable) hashtable.get(c(c1));

			if (hashtable1 == null) {
				hashtable1 = new Hashtable(1, 1.0F);
				hashtable.put(c(c1), hashtable1);
			}

			a(hashtable1, s, k + 1, obj);
		}
	}

	private final Character b(int k) {
		k &= 0xffff;
		return k >= 256 ? new Character((char) k) : i[k];
	}

	private final Short c(int k) {
		k &= 0xffff;
		return k >= 256 ? new Short((short) k) : j[k];
	}

	private final void b() {
		int k = h;

		if (g != 0) {
			while (k == h) {
				a(b);
			}
		}
	}
}