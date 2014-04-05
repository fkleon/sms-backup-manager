package de.leonhardt.sbm.gui.common;

import de.leonhardt.sbm.core.model.Settings;

/**
 * A service for reading and manipulating settings.
 * @author Frederik Leonhardt
 *
 */
public interface SettingsService {

	/**
	 * Persists the given arguments to settings store.
	 * @param countryCode
	 * @param languageCode
	 * @param exportIntl
	 */
	public void store(String countryCode, String languageCode, boolean exportIntl, boolean exportDupes);
	
	/**
	 * Returns the current settings.
	 * @return
	 */
	public Settings getSettings();
}
