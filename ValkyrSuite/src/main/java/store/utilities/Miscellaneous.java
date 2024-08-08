package store.utilities;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A class containing utilities that are used in this cache library.
 * 
 * @author Displee
 */
public class Miscellaneous {

	/**
	 * An array of characters.
	 */
	public static char[] aCharArray6385 = { '\u20ac', '\0', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020', '\u2021',
			'\u02c6', '\u2030', '\u0160', '\u2039', '\u0152', '\0', '\u017d', '\0', '\0', '\u2018', '\u2019', '\u201c',
			'\u201d', '\u2022', '\u2013', '\u2014', '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\0', '\u017e',
			'\u0178' };

	/**
	 * Converts a string to an byte array.
	 * 
	 * @param message The message.
	 * @return The byte array.
	 */
	public static byte[] getFormattedMessage(String message) {
		int length = message.length();
		byte[] bytes = new byte[length];
		for (int index = 0; length > index; index++) {
			int charValue = message.charAt(index);
			if ((charValue <= 0 || charValue >= 128) && (charValue < 160 || charValue > 255)) {
				if (charValue == 8364) {
					bytes[index] = (byte) -128;
				} else if (charValue == 8218) {
					bytes[index] = (byte) -126;
				} else if (charValue == 402) {
					bytes[index] = (byte) -125;
				} else if (charValue == 8222) {
					bytes[index] = (byte) -124;
				} else if (charValue == 8230) {
					bytes[index] = (byte) -123;
				} else if (charValue == 8224) {
					bytes[index] = (byte) -122;
				} else if (charValue == 8225) {
					bytes[index] = (byte) -121;
				} else if (charValue == 710) {
					bytes[index] = (byte) -120;
				} else if (charValue == 8240) {
					bytes[index] = (byte) -119;
				} else if (charValue == 352) {
					bytes[index] = (byte) -118;
				} else if (charValue == 8249) {
					bytes[index] = (byte) -117;
				} else if (charValue == 338) {
					bytes[index] = (byte) -116;
				} else if (charValue == 381) {
					bytes[index] = (byte) -114;
				} else if (charValue == 8216) {
					bytes[index] = (byte) -111;
				} else if (charValue == 8217) {
					bytes[index] = (byte) -110;
				} else if (charValue == 8220) {
					bytes[index] = (byte) -109;
				} else if (charValue == 8221) {
					bytes[index] = (byte) -108;
				} else if (charValue == 8226) {
					bytes[index] = (byte) -107;
				} else if (charValue == 8211) {
					bytes[index] = (byte) -106;
				} else if (charValue == 8212) {
					bytes[index] = (byte) -105;
				} else if (charValue == 732) {
					bytes[index] = (byte) -104;
				} else if (charValue == 8482) {
					bytes[index] = (byte) -103;
				} else if (charValue == 353) {
					bytes[index] = (byte) -102;
				} else if (charValue == 8250) {
					bytes[index] = (byte) -101;
				} else if (charValue == 339) {
					bytes[index] = (byte) -100;
				} else if (charValue == 382) {
					bytes[index] = (byte) -98;
				} else if (charValue == 376) {
					bytes[index] = (byte) -97;
				} else {
					bytes[index] = (byte) 63;
				}
			} else {
				bytes[index] = (byte) charValue;
			}
		}
		return bytes;
	}

	public static String method2122(byte[] is, int i, int i_11_) {
		char[] cs = new char[i_11_];
		int i_13_ = 0;
		for (int i_14_ = 0; i_14_ < i_11_; i_14_++) {
			int i_15_ = is[i + i_14_] & 0xff;
			if (i_15_ != 0) {
				if (i_15_ >= 128 && i_15_ < 160) {
					int i_16_ = aCharArray6385[i_15_ - 128];
					if (0 == i_16_) {
						i_16_ = 63;
					}
					i_15_ = i_16_;
				}
				cs[i_13_++] = (char) i_15_;
			}
		}
		return new String(cs, 0, i_13_);
	}

	/**
	 * A method used when decoding the script configurations.
	 * 
	 * @param i The value.
	 * @return The character.
	 */
	public static char method6566(byte i) {
		int index = i & 0xff;
		if (index == 0) {
			throw new IllegalArgumentException("Non cp1252 character 0x" + Integer.toString(i, 16) + " provided");
		}
		if (index >= 128 && index < 160) {
			int character = aCharArray6385[index - 128];
			if (character == 0) {
				character = 63;
			}
			index = character;
		}
		return (char) index;
	}

	public static int method6567(char character) {
		for (int i = 0; i < aCharArray6385.length; i++) {
			if (character == aCharArray6385[i]) {
				return i + Byte.MAX_VALUE + 1;
			}
		}
		return character;
	}

	/**
	 * Copies all bytes from the input stream to the output stream. Does not close
	 * or flush either stream.
	 *
	 * @param from the input stream to read from
	 * @param to   the output stream to write to
	 * @return the number of bytes copied
	 */
	public static long copy(InputStream from, OutputStream to) throws IOException {
		byte[] buf = new byte[0x1000];
		long total = 0;
		while (true) {
			int r = from.read(buf);
			if (r == -1) {
				break;
			}
			to.write(buf, 0, r);
			total += r;
		}
		return total;
	}

	public static int to317Hash(String name) {
		int hash = 0;
		name = name.toUpperCase();
		for (int i = 0; i < name.length(); i++) {
			hash = (hash * 61 + name.charAt(i)) - 32;
		}
		return hash;
	}

}