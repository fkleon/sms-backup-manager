package de.leonhardt.sbm.gui.pm;

import java.util.logging.Logger;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IconPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Service;
import org.beanfabrics.support.Validation;

import de.leonhardt.sbm.gui.service.FlagService;

/**
 * The Presentation Model for a countryCode and the associated flag icon.
 * The flag icon is always kept updated on changes to the country code,
 * if a FlagService has been provided to the PM's context.
 *
 * @author Frederik Leonhardt
 *
 */
public class CountryFlagPM extends AbstractPM {

	public TextPM countryCode = new TextPM();
	public IconPM countryFlagIcon = new IconPM();
	
	private FlagService flagService;
	
	public CountryFlagPM() {
		PMManager.setup(this);
		this.countryCode.setMandatory(true);
		this.countryCode.setEditable(true);
	}
	
	public CountryFlagPM(String countryCode) {
		this();
		setCountryCode(countryCode);
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode.setText(countryCode);
		normalizeCountryCodeAndUpdateFlag();
	}
	
	@Service
	public void setController(FlagService service) {
		this.flagService = service;
		
		// load the current flag and revalidate
		this.normalizeCountryCodeAndUpdateFlag();
		this.revalidateProperties();
	}
	
	/**
	 * Validates the country code.
	 * A country code is valid if it is non-empty and of length 2.
	 * 
	 * @return
	 */
	@Validation(path="countryCode")
	public boolean validateCountryCode() {
		String cCode = this.countryCode.getText();
		return (cCode != null && !cCode.isEmpty() && cCode.length()==2);
	}
	
	/**
	 * Normalizes the country code (to upper case + cut to length 2),
	 * and reloads the flag icon.
	 */
	@OnChange(path="countryCode")
	public void normalizeCountryCodeAndUpdateFlag() {
		String cText = normalizeCountryCode(countryCode.getText());
		this.countryCode.setText(cText);
		if (flagService != null) {
			this.countryFlagIcon.setIcon(flagService.getFlag(countryCode.getText()));
		} else {
			Logger.getAnonymousLogger().fine("No FlagService configured.");
		}
	}
	
	/**
	 * Returns the normalized country code.
	 * Returns default country code, if given country code is null.
	 * 
	 * @param countryCode
	 * @return
	 */
	private String normalizeCountryCode(String countryCode) {
		if (countryCode != null) {
			return cutToLength(countryCode.toUpperCase(),2);
		} else {
			return ""; // some default
		}
	}
	
	/**
	 * Cuts the given string to a given length.
	 * @param text
	 * @param length
	 * @return
	 */
	private String cutToLength(String text, int length) {
		if (text != null && !text.isEmpty() && text.length() > 2) {
			return text.substring(0,2);
		} else {
			return text;
		}
	}
}