package de.leonhardt.sbm.gui.common.resource;

import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import lombok.extern.log4j.Log4j2;


/**
 * This class is responsible for loading the flag icons
 *
 * @author Frederik Leonhardt
 *
 */
@Log4j2
public class FlagLoader extends ResourceLoader implements FlagService {

	private static String resPath = "/images/flagIcons/%s.png";


	/**
	 * Returns a country flag based on the given country code.
	 * Returns a default flag, if no flag could be found.
	 *
	 * @param countryCode
	 * @return ImageIcon with flag for the country or default flag
	 */
	@Override
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

		if ((flagIcon = getFlagIcon("default")) == null) {
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
			log.debug("Could not find flag for country '{}' (flagURL null)", normalizedCountryCode);
			return null;
		} else {
			try {
				return new ImageIcon(ImageIO.read(flagURL), normalizedCountryCode);
			} catch (Exception ioe) {
				log.debug("Could not read flag for country '{}': {}", normalizedCountryCode, ioe.toString(), ioe);
				return null;
			}
//			return new ImageIcon(flagURL, normalizedCountryCode);
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
