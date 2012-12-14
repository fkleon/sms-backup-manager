package de.leonhardt.sbm.gui.model;

import java.util.Locale;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import de.leonhardt.sbm.gui.service.LocaleProvider;
import de.leonhardt.sbm.gui.service.SettingsService;

/**
 * Application-wide program settings.
 * 
 * @author Frederik Leonhardt
 *
 */
public class Settings implements SettingsService, LocaleProvider {

	private static Settings _instance;
	
	private Logger log = Logger.getLogger("Settings");
	private Preferences prefs;

	private Locale currentLocale;
	
	private boolean exportIntl;
	private boolean exportDupes;
		
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
		prefs.put("language", this.currentLocale.getLanguage());
		prefs.put("countryCode", this.currentLocale.getCountry());
		prefs.put("regionCode", this.currentLocale.getVariant());

		// settings
		prefs.putBoolean("exportInternational", this.exportIntl);
		prefs.putBoolean("exportDuplicates", this.exportDupes);
		
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
		
		Locale defLocale = Locale.getDefault();
		String languageCode = prefs.get("language", defLocale.getLanguage());
		String countryCode = prefs.get("countryCode", defLocale.getCountry());
		String regionCode = prefs.get("regionCode", defLocale.getVariant());
		
		this.currentLocale = new Locale(languageCode, countryCode, regionCode);
		
		this.exportIntl = prefs.getBoolean("exportInternational", false);
		this.exportDupes = prefs.getBoolean("exportDuplicates", false);
		
		log.info("Loaded prefs: "+this);
	}
	
	@Override
	public String toString() {
		return "Settings [languageCode=" + currentLocale.getLanguage() + ", countryCode="
				+ currentLocale.getCountry() + ", regionCode=" + currentLocale.getVariant() + ", exportIntl="
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
	
	public String getLanguageCode() {
		return this.currentLocale.getLanguage();
	}

	public String getCountryCode() {
		return this.currentLocale.getCountry();
	}
	
	public String getRegionCode() {
		return this.currentLocale.getVariant();
	}
	
	public void setExportInternationalNumbers(boolean exportIntl) {
		this.exportIntl = exportIntl;
	}
	
	public boolean getExportInternationalNumbers() {
		return this.exportIntl;
	}

	public boolean isExportDupes() {
		return exportDupes;
	}

	public void setExportDupes(boolean exportDupes) {
		this.exportDupes = exportDupes;
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
			boolean exportIntl, boolean exportDupes) {
		setLocale(languageCode, countryCode, "");
		setExportInternationalNumbers(exportIntl);
		setExportDupes(exportDupes);
		save();
	}
	
	/**
	 * Implementation of service pattern method
	 */
	@Override
	public Settings getSettings() {
		return this;
	}

	@Override
	public void setLocale(Locale locale) {
		ensureNotNull(locale);
		this.currentLocale = locale;
	}

	@Override
	public void setLocale(String languageCode, String countryCode,
			String regionCode) {
		ensureNotNull(languageCode);
		ensureLength(languageCode, 2);
		ensureNotNull(countryCode);
		ensureLength(countryCode, 2);
		ensureNotNull(regionCode);
		this.currentLocale = new Locale(languageCode, countryCode, regionCode);
	}

	@Override
	public Locale getLocale() {
		return this.currentLocale;
	}
}
