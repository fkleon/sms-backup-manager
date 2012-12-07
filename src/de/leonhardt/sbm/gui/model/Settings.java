package de.leonhardt.sbm.gui.model;

import java.util.Locale;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Settings {

	private Logger log = Logger.getLogger("Settings");
	private Preferences prefs;
	
	private boolean isDirty;
	
	private String languageCode;
	private String countryCode;
	private String regionCode;
	
	private boolean exportIntl;
	
	public Settings() {
		this.prefs = Preferences.userNodeForPackage(this.getClass());
		load();
	}

	public void save() {
		if (!isDirty()) {
			return;
		}
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
		setDirty();
	}
	
	public String getLanguageCode() {
		return this.languageCode;
	}
	
	public void setCountryCode(String countryCode) {
		ensureNotNull(countryCode);
		ensureLength(countryCode, 2);
		this.countryCode = countryCode.toUpperCase();
		setDirty();
	}
	
	public String getCountryCode() {
		return this.countryCode;
	}
	
	public void setRegionCode(String regionCode) throws IllegalArgumentException {
		ensureNotNull(regionCode);
		ensureLength(regionCode, 2);
		this.regionCode = regionCode.toUpperCase();
		setDirty();
	}
	
	public String getRegionCode() {
		return this.regionCode;
	}
	
	public void setExportInternationalNumbers(boolean exportIntl) {
		this.exportIntl = exportIntl;
		setDirty();
	}
	
	public boolean getExportInternationalNumbers() {
		return this.exportIntl;
	}

	private Preferences getPrefs() {
		return this.prefs;
	}
	
	public boolean isDirty() {
		return this.isDirty;
	}
	
	private void setDirty() {
		this.isDirty = true;
	}
	
	private void ensureNotNull(Object obj) throws IllegalArgumentException {
		if (obj==null)
			throw new IllegalArgumentException("obj is null");
	}
	
	private void ensureLength(String str, int length) throws IllegalArgumentException {
		if (str.length() != length) {
			throw new IllegalArgumentException(String.format("Expected string '%s' to be of length %d",str,length));
		}
	}
}
