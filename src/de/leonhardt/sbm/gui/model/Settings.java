package de.leonhardt.sbm.gui.model;

import java.util.Locale;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import de.leonhardt.sbm.gui.pm.SettingsService;

/**
 * Application-wide program settings.
 * 
 * @author Frederik Leonhardt
 *
 */
public class Settings implements SettingsService {

	private static Settings _instance;
	
	private Logger log = Logger.getLogger("Settings");
	private Preferences prefs;
		
	private String languageCode;
	private String countryCode;
	private String regionCode;
	
	private boolean exportIntl;
		
	private Settings() {
		this.prefs = Preferences.userNodeForPackage(this.getClass());
		load();
	}

	/**
	 * Settings is singleton, there is only one instance.
	 * @return the instance
	 */
	public static Settings getInstance() {
		if (_instance == null) {
			_instance = new Settings();
		}
		return _instance;
	}
	
	/**
	 * Persists the current settings
	 */
	public void save() {
		// validate input
		
		// store input
		Preferences prefs = getPrefs();

		// locale
		prefs.put("language", this.languageCode);
		prefs.put("countryCode", this.countryCode);
		prefs.put("regionCode", this.regionCode);

		// settings
		prefs.putBoolean("exportInternational", this.exportIntl);
		
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			log.warning("Could not persist settings: " + e.toString());
		}
	}
	
	/**
	 * Loads the settings from persistent storage
	 */
	public void load() {
		Preferences prefs = getPrefs();
		
		Locale curLocale = Locale.getDefault();
		this.languageCode = prefs.get("language", curLocale.getLanguage());
		this.countryCode = prefs.get("countryCode", curLocale.getCountry());
		this.regionCode = prefs.get("regionCode", curLocale.getVariant());
		
		this.exportIntl = prefs.getBoolean("exportInternational", false);
		
		log.info("Loaded prefs: "+this);
	}
	
	@Override
	public String toString() {
		return "Settings [languageCode=" + languageCode + ", countryCode="
				+ countryCode + ", regionCode=" + regionCode + ", exportIntl="
				+ exportIntl + "]";
	}

	/**
	 * Removes all saved preferences and resets to default.
	 */
	public void clear() {
		try {
			getPrefs().clear();
		} catch (BackingStoreException e) {
			log.warning("Could not clear settings: " + e.toString());
		}
	}
	
	public void setLanguageCode(String languageCode) {
		ensureNotNull(languageCode);
		ensureLength(languageCode, 2);
		this.languageCode = languageCode.toLowerCase();
	}
	
	public String getLanguageCode() {
		return this.languageCode;
	}
	
	public void setCountryCode(String countryCode) {
		ensureNotNull(countryCode);
		ensureLength(countryCode, 2);
		this.countryCode = countryCode.toUpperCase();
	}
	
	public String getCountryCode() {
		return this.countryCode;
	}
	
	public void setRegionCode(String regionCode) throws IllegalArgumentException {
		ensureNotNull(regionCode);
		ensureLength(regionCode, 2);
		this.regionCode = regionCode.toUpperCase();
	}
	
	public String getRegionCode() {
		return this.regionCode;
	}
	
	public void setExportInternationalNumbers(boolean exportIntl) {
		this.exportIntl = exportIntl;
	}
	
	public boolean getExportInternationalNumbers() {
		return this.exportIntl;
	}

	/**
	 * Returns the preference store
	 * @return
	 */
	private Preferences getPrefs() {
		return this.prefs;
	}
	
	/**
	 * Ensures that the given object is not null
	 * @param obj
	 * @throws IllegalArgumentException
	 */
	private void ensureNotNull(Object obj) throws IllegalArgumentException {
		if (obj==null)
			throw new IllegalArgumentException("obj is null");
	}
	
	/**
	 * Ensures that the given string is of given length
	 * @param str
	 * @param length
	 * @throws IllegalArgumentException
	 */
	private void ensureLength(String str, int length) throws IllegalArgumentException {
		if (str.length() != length) {
			throw new IllegalArgumentException(String.format("Expected string '%s' to be of length %d",str,length));
		}
	}

	/**
	 * Implementation of service pattern method
	 */
	@Override
	public void store(String countryCode, String languageCode,
			boolean exportIntl) {
		setCountryCode(countryCode);
		setLanguageCode(languageCode);
		setExportInternationalNumbers(exportIntl);
		save();
	}
	
	/**
	 * Implementation of service pattern method
	 */
	@Override
	public Settings getSettings() {
		return this;
	}
}
