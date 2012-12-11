package de.leonhardt.sbm;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

import de.leonhardt.sbm.convert.SmsesConvert;
import de.leonhardt.sbm.gui.service.MessageService;
import de.leonhardt.sbm.model.Contact;
import de.leonhardt.sbm.model.Message;
import de.leonhardt.sbm.store.MessageStore;
import de.leonhardt.sbm.store.SortedMessageStore;
import de.leonhardt.sbm.util.Utils;
import de.leonhardt.sbm.util.Utils.IdGenerator;
import de.leonhardt.sbm.util.comparator.ContactNameComparator;
import de.leonhardt.sbm.xml.model.Sms;
import de.leonhardt.sbm.xml.model.Smses;

public class BackupManager implements MessageService {

	private Logger log;
	private PhoneNumberParser pnp;
	private Map<Contact,MessageStore> conversations;
	private SmsesConvert messageConverter;
	
	private String countryCode;
	private String languageCode;
	private String regionCode;
	
	/**
	 * Creates a new BackupManager with given country and locale
	 * 
	 * @param countryCode
	 * @param languageCode
	 * @param regionCode
	 */
	public BackupManager(String countryCode, String languageCode, String regionCode) {
		initLocale(countryCode, languageCode, regionCode);
		init();
	}
	
	/**
	 * Creates a new default BackupManager with
	 * - Country: DE
	 * - Locale: de-DE
	 */
	public BackupManager() {
		initLocale("DE","de","DE");
		init();
	}
	
	/**
	 * Initializes Locale and PNP
	 * 
	 * @param countryCode
	 * @param languageCode
	 * @param regionCode
	 */
	public void initLocale(String countryCode, String languageCode, String regionCode) {
		this.countryCode = countryCode;
		this.languageCode = languageCode;
		this.regionCode = regionCode;
		
		this.pnp = new PhoneNumberParser(countryCode, languageCode, regionCode);
	}
	
	/**
	 * Initializes MessageStore, ContactStore and Logger
	 */
	private void init() {
		//this.idGen = Utils.getDefaultIdGenerator();
		this.conversations = Collections.synchronizedMap(new HashMap<Contact, MessageStore>());
		this.messageConverter = SmsesConvert.getInstance();
		
		this.log = Logger.getLogger("BackupManager");
	}
	
	/**
	 * Imports all messages in the given Smses object.
	 * 
	 * @param smses
	 */
	public void importMessages(Smses smses) {
		if (smses == null || smses.getSms() == null) {
			log.warning("Can not import null object.");
			return;
		}
		
		// while importing, assign IDs and build Contacts
		for (Sms sms: smses.getSms()) {
			// add contact and message
			Contact contact = getNormalizedContact(sms);
			
			try {
				Message msg = messageConverter.toMessage(sms, contact);
				putMessage(contact, msg);
			} catch (Exception e) {
				log.warning("Could not convert SMS to Message: " + e.toString());
			}
		}
		
		log.info(String.format("Imported %d messages (+%d duplicates)",getMessages().size(),getMessages().countDuplicates()));
	}
	
	private void putMessage(Contact contact, Message message) {
		MessageStore ms;
		if ((ms = conversations.get(contact)) != null) {
			ms.add(message);
		} else {
			ms = new MessageStore();
			ms.add(message);
			conversations.put(contact, ms);
		}
	}
	
	private Contact getNormalizedContact(Sms message) {
		return getNormalizedContact(message.getContactName(), message.getAddress());
	}
	
	private Contact getNormalizedContact(String name, String address) {
		String contactName = name.trim();
		String addressIntl = pnp.getInternationalFormat(address);
		String countryCode = pnp.getCountryCode(address);
		Contact contact = new Contact(contactName, addressIntl, countryCode);
		return contact;
	}
	
	protected Map<Contact, MessageStore> getCS() {
		return this.conversations;
	}
	
	/**
	 * Returns sorted collection of all contacts.
	 * Sorted by name, then number, descending.
	 * 
	 * @return
	 */
	public Collection<Contact> getContacts() {
		Collection<Contact> col = new TreeSet<Contact>(new ContactNameComparator());
		col.addAll(this.conversations.keySet());
		return col;
	}
	
	/**
	 * Returns sorted collection of all messages with a given contact.
	 * Sorted by date, descending.
	 * 
	 * @param c
	 * @return
	 */
	public MessageStore getMessages(Contact c) {
		SortedMessageStore sms = new SortedMessageStore();
		
		if (this.conversations.containsKey(c)) {
			sms.addAll(conversations.get(c));
		}
		
		return sms;
	}

	@Override
	public String toString() {
		return "BackupManager [countryCode="+ countryCode + ", locale=" + languageCode + "-" + regionCode
				+ ", messages = " + getMessages().size() + ", contacts = " + conversations.keySet().size() + "]";
	}

	@Override
	public MessageStore getMessages() {
		SortedMessageStore allMessages = new SortedMessageStore();
		
		for (MessageStore ms: getCS().values()) {
			allMessages.addAll(ms);
		}

		return allMessages;
	}
	
	public void clear() {
		conversations.clear();
	}
}
