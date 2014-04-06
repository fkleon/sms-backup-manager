package de.leonhardt.sbm.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

import de.leonhardt.sbm.core.model.Contact;
import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.core.service.ContactService;
import de.leonhardt.sbm.core.service.MessageService;
import de.leonhardt.sbm.core.store.MessageStore;
import de.leonhardt.sbm.core.store.SortedMessageStore;
import de.leonhardt.sbm.core.util.ContactNameComparator;
import de.leonhardt.sbm.core.util.Utils;
import de.leonhardt.sbm.core.util.Utils.IdGenerator;

/**
 * The BackupManager is the main entry point for GUI and CMD.
 * There should be only one BackupManager instance per application.
 * 
 * It manages all messages, contacts and conversations.
 * 
 * @author Frederik Leonhardt
 *
 */
public class BackupManager implements MessageService, ContactService {

	private Logger log;
	private Map<Contact,MessageStore> conversations;
	private IdGenerator idGen;
	
	/**
	 * Creates a new BackupManager
	 */
	public BackupManager() {
		init();
	}
	
	/**
	 * Initializes MessageStore and Logger
	 */
	private void init() {
		this.conversations = Collections.synchronizedMap(new HashMap<Contact, MessageStore>());
		this.idGen = Utils.getDefaultIdGenerator();
		this.log = Logger.getLogger("BackupManager");
	}
	
	/**
	 * Imports all messages in the given collection,
	 * also assigns IDs to them.
	 * 
	 * @param messages
	 */
	public void importMessages(Collection<Message> messages) {
		if (messages == null) {
			log.warning("Can not import null collection.");
			return;
		}
		
		for (Message message: messages) {
			importMessage(message);
		}
		
		log.info(String.format("%d messages in store (+ %d duplicates).",getMessages().size(),getMessages().countDuplicates()));
	}
	
	/**
	 * Imports a given message,
	 * also assigns ID to it.
	 * 
	 * @param message
	 */
	public Message importMessage(Message message) {
		if (message == null) {
			log.warning("Can not import null message.");
			return null;
		}
		
		putMessage(message.getContact(), idGen.assignNextId(message));
		
		return message;
	}
	
	/**
	 * Adds a message to the appropriate message store for a given contact.
	 * 
	 * @param contact
	 * @param message
	 */
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
	
	/**
	 * Returns the underlying conversation map.
	 * Should only be used for testing.
	 * 
	 * @return
	 */
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

	@Override
	public Contact findContact(String contactName, String intlAddress) {
		Contact toFind = new Contact(contactName, intlAddress);
		
		for (Contact c: conversations.keySet()) {
			if (c.equals(toFind)) {
				return c;
			}
		}
		
		return null;
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
		return "BackupManager [messages = " + getMessages().size() + ", contacts = " + conversations.keySet().size() + "]";
	}

	@Override
	public MessageStore getMessages() {
		SortedMessageStore allMessages = new SortedMessageStore();
		
		for (MessageStore ms: getCS().values()) {
			allMessages.addAll(ms);
		}

		return allMessages;
	}
	
	/**
	 * Clears the underlying conversation store.
	 * Removes all contacts and messages.
	 */
	public void clear() {
		conversations.clear();
	}

	@Override
	public int getMessageCount() {
		int messageCount = 0;
		
		for (MessageStore ms: conversations.values()) {
			messageCount += ms.size();
		}
		
		return messageCount;
	}
	
	@Override
	public int getContactCount() {
		int contactCount = conversations.keySet().size();
		return contactCount;
	}

}
