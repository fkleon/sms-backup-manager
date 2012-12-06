package de.leonhardt.sbm;

import java.util.Locale;
import java.util.logging.Logger;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

/**
 * The PhoneNumberParser performs operations on phone numbers.
 * It can be initialized with a country code and locale to process national number formats.
 * 
 * @author Frederik Leonhardt
 *
 */
public class PhoneNumberParser {
	
	/**
	 * Specifies country code to use for non-international numbe parsing
	 */
	String countryCode = null; // ISO 3166-1 two-letter country code
	
	/**
	 * Language and region codes are used to validate phone numbers
	 * Usual format is "languageCode-regionCode", e.g. en-US
	 */
	String languageCode = "en"; // Default languageCode to English if nothing is
								// entered.
	String regionCode = "";

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger("PhoneNumberParser");

	/**
	 * Utils
	 */
	private PhoneNumberUtil phoneUtil;
	private PhoneNumberOfflineGeocoder phoneGeocoder;
	
	
	/**
	 * Creates default phone number parser.
	 * Assumes input to be formated according to international phone number format.
	 * Locale is en-US.
	 */
	public PhoneNumberParser() {
		// null country code assumes international phone numbers, 
		// "ZZ" also possible for that
		init(null, "en", "US");
	}
	
	/**
	 * Creates phone number parser by given values.
	 * 
	 * @param countryCode
	 * @param languageCode
	 * @param regionCode
	 */
	public PhoneNumberParser(String countryCode, String languageCode, String regionCode) {
		init(countryCode, languageCode, regionCode);
	}
	
	/**
	 * Initializes fields
	 * 
	 * @param countryCode
	 * @param languageCode
	 * @param regionCode
	 */
	private void init(String countryCode, String languageCode, String regionCode) {
		this.phoneUtil = PhoneNumberUtil.getInstance();
		this.phoneGeocoder = PhoneNumberOfflineGeocoder.getInstance();
		this.countryCode = countryCode;
		this.languageCode = languageCode;
		this.regionCode = regionCode;
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
			log.warning("Could not parse phone number '" + phoneNumber + "'. "
						+ "[country=" + this.countryCode
						+ ", locale=" + languageCode + "-" + regionCode +  "]"
						+ " Error: " + e.getMessage());
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
			log.warning("Could not parse phone number '" + phoneNumber + "'. "
						+ "[country=" + this.countryCode
						+ ", locale=" + languageCode + "-" + regionCode +  "]"
						+ " Error: " + e.getMessage());
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
			geoInfo = phoneGeocoder.getDescriptionForNumber(number, new Locale(languageCode, regionCode));
		} catch (NumberParseException e) {
			log.warning("Could not parse phone number '" + phoneNumber + "'. "
					+ "[country=" + this.countryCode
					+ ", locale=" + languageCode + "-" + regionCode +  "]"
					+ " Error: " + e.getMessage());
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
		return phoneUtil.parseAndKeepRawInput(phoneNumber, this.countryCode);
	}
}
