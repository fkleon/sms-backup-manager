package de.leonhardt.sbm.gui.resource;

import java.net.URL;

public abstract class ResourceLoader {

	public ResourceLoader() {
		
	}
	
	/**
	 * Determines URL of given resource.
	 * 
	 * @param resPath
	 * @return null, if resource not found
	 */
	protected URL getResourceURL(String resPath) {
		return ResourceLoader.class.getResource(resPath);
	}
	
	protected String normalize(String str) {
		return str.toLowerCase();
	}
	
}
