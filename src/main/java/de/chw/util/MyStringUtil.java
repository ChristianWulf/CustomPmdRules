package de.chw.util;

public final class MyStringUtil {

	private MyStringUtil() {
		// utility class
	}

	public static String repeat(String string, int numRepeats) {
		StringBuilder builder = new StringBuilder(string.length() * numRepeats);
		for (int i = 0; i < numRepeats; i++) {
			builder.append(string);
		}
		return builder.toString();
	}

}
