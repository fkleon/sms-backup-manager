package de.leonhardt.sbm.core.model;

import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import de.leonhardt.sbm.core.util.LocaleProvider;
import de.leonhardt.sbm.core.util.StringUtils;
import de.leonhardt.sbm.gui.common.SettingsService;

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
		Objects.requireNonNull(locale, "Locale must not be null.");
		this.currentLocale = locale;
	}

	@Override
	public void setLocale(String languageCode, String countryCode,
			String regionCode) {
		Objects.requireNonNull(languageCode, "Language code must not be null.");
		StringUtils.ensureLength(languageCode, 2);
		Objects.requireNonNull(countryCode, "Country code must not be null.");
		StringUtils.ensureLength(countryCode, 2);
		Objects.requireNonNull(regionCode, "Region code must not be null.");
		this.currentLocale = new Locale(languageCode, countryCode, regionCode);
	}

	@Override
	public Locale getLocale() {
		return this.currentLocale;
	}
}
