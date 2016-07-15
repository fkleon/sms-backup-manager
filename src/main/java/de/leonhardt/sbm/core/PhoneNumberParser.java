package de.leonhardt.sbm.core;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import de.leonhardt.sbm.core.model.Settings;
import de.leonhardt.sbm.core.util.LocaleProvider;
import lombok.extern.log4j.Log4j2;

/**
 * The PhoneNumberParser performs operations on phone numbers.
 * It uses the currently available locale from {@link LocaleProvider} to process national number formats.
 *
 * There are special countryCodes permitted:
 * - null and "ZZ" are permitted (assumes international numbers).
 *
 * Language and region code are used for phone number validation.
 *
 * @author Frederik Leonhardt
 *
 */
@Log4j2
public class PhoneNumberParser {

	/**
	 * Utils
	 */
	private PhoneNumberUtil phoneUtil;
	private PhoneNumberOfflineGeocoder phoneGeocoder;


	/**
	 * Creates default phone number parser.
	 * Assumes input to be formated according to current locale settings specified by {@link LocaleProvider}.
	 */
	public PhoneNumberParser() {
		this.phoneUtil = PhoneNumberUtil.getInstance();
		this.phoneGeocoder = PhoneNumberOfflineGeocoder.getInstance();
	}

	/**
	 * Returns the international format of a given phone number string, uses countryCode if necessary.
	 *
	 * @param phoneNumber
	 * @return international format of input number or original input on parsing error
	 */
	public String getInternationalFormat(String phoneNumber) {
		String internationalFormat = phoneNumber;

		try {
			PhoneNumber number = parseToPhoneNumber(phoneNumber);

			// no validation yet
			//boolean isNumberValid = phoneUtil.isValidNumber(number);

			internationalFormat = phoneUtil.format(number, PhoneNumberFormat.INTERNATIONAL);
		} catch (NumberParseException e) {
			log.debug("Could not parse phone number '{}'. [locale={}] Error: {}",
					phoneNumber, Settings.getInstance().getLocale().toString(), e.getMessage());
		}

		return internationalFormat;
	}

	/**
	 * Returns the country code of a given phone number string.
	 *
	 * @param phoneNumber
	 * @return country code of input number or original input on parsing error
	 */
	public String getCountryCode(String phoneNumber) {
		String countryCode = "ZZ";

		try {
			PhoneNumber number = parseToPhoneNumber(phoneNumber);
			countryCode = phoneUtil.getRegionCodeForNumber(number);
		} catch (NumberParseException e) {
			log.debug("Could not parse phone number '{}'. [locale={}] Error: {}",
					phoneNumber, Settings.getInstance().getLocale().toString(), e.getMessage());
		}

		return countryCode;
	}

	/**
	 * Lookup geo information from offline database.
	 *
	 * @param phoneNumber
	 * @return geo information string or null
	 */
	public String getGeoInformation(String phoneNumber) {
		String geoInfo = null;
		try {
			PhoneNumber number = parseToPhoneNumber(phoneNumber);
			geoInfo = phoneGeocoder.getDescriptionForNumber(number, Settings.getInstance().getLocale());
		} catch (NumberParseException e) {
			log.debug("Could not parse phone number '{}'. [locale={}] Error: {}",
					phoneNumber, Settings.getInstance().getLocale().toString(), e.getMessage());
		}

		return geoInfo;
	}

	/**
	 * Parses a given String to a PhoneNumber object.
	 *
	 * @param phoneNumber
	 * @return
	 * @throws NumberParseException
	 */
	private PhoneNumber parseToPhoneNumber(String phoneNumber) throws NumberParseException {
		return phoneUtil.parseAndKeepRawInput(phoneNumber, Settings.getInstance().getLocale().getCountry());
	}
}
