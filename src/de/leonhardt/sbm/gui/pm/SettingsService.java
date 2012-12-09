package de.leonhardt.sbm.gui.pm;

import de.leonhardt.sbm.gui.model.Settings;

/**
 * A controller view to the settings.
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
	public void store(String countryCode, String languageCode, boolean exportIntl);
	
	/**
	 * Returns the current settings.
	 * @return
	 */
	public Settings getSettings();
}
