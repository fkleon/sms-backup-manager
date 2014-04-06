package de.leonhardt.sbm.core.util;

/**
 * Helpers for strings.
 * 
 * @author Frederik Leonhardt
 *
 */
public class StringUtils {
	
	/**
	 * Ensures that the given string is of given length.
	 * 
	 * @param str
	 * @param length
	 * @throws IllegalArgumentException
	 */
	public static String ensureLength(final String str, final int length) throws IllegalArgumentException {
		if (str.length() != length) {
			throw new IllegalArgumentException(String.format("Expected string '%s' to be of length %d",str,length));
		}
		return str;
	}
}
