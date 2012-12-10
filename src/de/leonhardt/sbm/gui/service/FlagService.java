package de.leonhardt.sbm.gui.service;

import javax.swing.ImageIcon;

/**
 * A service for flag icon lookups.
 * 
 * @author Frederik Leonhardt
 *
 */
public interface FlagService {

	public ImageIcon getFlag(String countryCode);
	
}
