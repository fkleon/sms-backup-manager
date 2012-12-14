package de.leonhardt.sbm.gui.service;

import java.util.Locale;

/**
 * Provider class for the current locale.
 * 
 * @author Frederik Leonhardt
 */
public interface LocaleProvider {
	
	/**
	 * Sets the current locale.
	 * 
	 * @param locale
	 */
	public void setLocale(Locale locale);
	
	/**
	 * Sets the current locale.
	 * Any combination is allowed, even non-existing codes.
	 * Use country code "ZZ" for international.
	 * 
	 * @param languageCode
	 * @param countryCode
	 * @param regionCode
	 */
	public void setLocale(String languageCode, String countryCode, String regionCode);

	/**
	 * Returns the current locale.
	 * Returns system default locale, if no locale set yet.
	 * 
	 * @return
	 */
	public Locale getLocale();

}
