package de.leonhardt.sbm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;


import de.leonhardt.sbm.gui.renderer.ContactListCellRenderer;
import de.leonhardt.sbm.store.ContactStore;
import de.leonhardt.sbm.store.MessageStore;
import de.leonhardt.sbm.store.ObjectStore;
import de.leonhardt.sbm.util.Utils;
import de.leonhardt.sbm.util.Utils.IdGenerator;
import de.leonhardt.sbm.util.comparator.ContactNameComparator;
import de.leonhardt.sbm.util.comparator.MessageDateComparator;
import de.leonhardt.sbm.xml.model.Contact;
import de.leonhardt.sbm.xml.model.Sms;
import de.leonhardt.sbm.xml.model.Smses;

public class BackupManager {

	private Logger log;
	private PhoneNumberParser pnp;
	private MessageStore ms;
	//private ContactStore cs;
	private Map<Contact,MessageStore> conversations;
	private IdGenerator idGen;
	
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
		this.idGen = Utils.getDefaultIdGenerator();
		this.ms = new MessageStore(idGen);
		//this.cs = new ContactStore(idGen);
		this.conversations = new HashMap<Contact, MessageStore>();
		
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
		for (Sms message: smses.getSms()) {
			// add message
			ms.addObject(message);
		
			// add contact
			Contact contact = getNormalizedContact(message);
			
			putMessage(contact, message);
		}
	}
	
	private void putMessage(Contact contact, Sms message) {
		MessageStore ms;
		if ((ms = conversations.get(contact)) != null) {
			ms.add(message);
		} else {
			ms = new MessageStore(this.idGen);
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
	
	public MessageStore getMS() {
		return this.ms;
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
	public Collection<Sms> getMessages(Contact c) {
		List<Sms> messages = new ArrayList<Sms>();
		
		if (this.conversations.containsKey(c)) {
			messages.addAll(this.conversations.get(c));
			
			// messages in a MessageStore are assumed to be sorted ascending,
			// so we just revert the list
			//Collections.reverse(messages);
			Collections.sort(messages, new MessageDateComparator(true));
			
			return messages;
		} else {
			return messages;
		}
	}

	@Override
	public String toString() {
		return "BackupManager [countryCode="+ countryCode + ", locale=" + languageCode + "-" + regionCode
				+ ", messages = " + ms.size() + ", contacts = " + conversations.keySet().size() + "]";
	}
}
