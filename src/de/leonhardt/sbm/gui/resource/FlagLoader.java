package de.leonhardt.sbm.gui.resource;

import java.net.URL;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

/**
 * This class is responsible for loading the flag icons
 * 
 * @author Frederik Leonhardt
 *
 */
public class FlagLoader extends ResourceLoader {
	
	private static String resPath = "/resources/images/flagIcons/%s.png";
	
	/**
	 * Returns a country flag based on the given country code.
	 * Returns a default flag, if no flag could be found.
	 * 
	 * @param countryCode
	 * @return ImageIcon with flag for the country or default flag
	 */
	public ImageIcon getFlag(String countryCode) {
		ImageIcon imageIcon = null;
		
		if (countryCode != null && countryCode.length() > 0) {
			String cc = normalize(countryCode); // normalize countryCode
			
			// lookup flag
			imageIcon = getFlagIcon(cc);
		}
		
		if (imageIcon == null) {
			return getDefaultFlagIcon();
		} else {
			return imageIcon;
		}
	}
	
	/**
	 * Returns the default flag or an empty ImageIcon,
	 * but never null.
	 * 
	 * @return
	 */
	private ImageIcon getDefaultFlagIcon() {
		ImageIcon flagIcon;
		
		if ((flagIcon = getFlag("default")) == null) {
			flagIcon = new ImageIcon();
			flagIcon.setDescription("n/a");
		}
		
		return flagIcon;
	}
	
	/**
	 * Returns an image icon containing the flag for a country.
	 * 
	 * @param normalizedCountryCode the normalized country code (lowercase)
	 * @return ImageIcon or null, if no icon could be found
	 */
	private ImageIcon getFlagIcon(String normalizedCountryCode) {
		URL flagURL = getResourceURL(buildResPath(normalizedCountryCode));
		if (flagURL == null) {
			Logger.getAnonymousLogger().fine("Could not find flag for country '"+normalizedCountryCode+"' (flagURL null)");
			return null;
		} else {
			return new ImageIcon(flagURL, normalizedCountryCode);
		}
	}
	
	/**
	 * Formats the resource path.
	 * 
	 * @param countryCode
	 * @return
	 */
	private String buildResPath(String countryCode) {
		return String.format(resPath, countryCode);
	}
}
