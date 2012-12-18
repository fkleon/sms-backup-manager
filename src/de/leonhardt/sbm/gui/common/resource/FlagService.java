package de.leonhardt.sbm.gui.common.resource;

import javax.swing.ImageIcon;

/**
 * A service for flag icon lookups.
 * 
 * @author Frederik Leonhardt
 *
 */
public interface FlagService {

	/**
	 * Returns the ImageIcon for a given countryCode.
	 * @param countryCode
	 * @return
	 */
	public ImageIcon getFlag(String countryCode);
	
}
