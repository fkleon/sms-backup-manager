package de.leonhardt.sbm.core.util;

/**
 * Helper for objects.
 * 
 * @author Frederik Leonhardt
 *
 */
public class ObjectUtils {

	/**
	 * Ensures that the given object is not null.
	 * 
	 * @param o
	 * @return the object
	 * @throws IllegalArgumentException if the object is null
	 */
	public static <E> E ensureNotNull(E o) throws IllegalArgumentException {
		if (o == null) throw new IllegalArgumentException("Object should not be null.");
		return o;
	}
}
