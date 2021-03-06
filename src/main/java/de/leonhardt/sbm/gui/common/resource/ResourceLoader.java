package de.leonhardt.sbm.gui.common.resource;

import java.net.URL;

/**
 * Abstract class to simplify resource loading from
 * the application archive.
 *
 * @author Frederik Leonhardt
 *
 */
public class ResourceLoader {

	/**
	 * Determines URL of given resource.
	 *
	 * @param resPath
	 * @return null, if resource not found
	 */
	protected URL getResourceURL(String resPath) {
		return ResourceLoader.class.getResource(resPath);
	}

	/**
	 * Normalises the given string.
	 *
	 * @param str
	 * @return
	 */
	protected String normalize(String str) {
		return str.toLowerCase().trim();
	}

}
