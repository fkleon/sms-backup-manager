package de.leonhardt.sbm;

import java.util.Collection;

import de.leonhardt.sbm.core.model.Contact;
import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.core.service.ContactService;
import de.leonhardt.sbm.core.service.MessageService;
import de.leonhardt.sbm.core.store.MessageStore;

/**
 * A dummy implementation of MessageService and ContactService.
 * Returns random contacts and messages.
 * 
 * @author Frederik Leonhardt
 *
 */
public class BackupManagerDummy implements MessageService, ContactService {

	// primitive contact and message stores
	private Collection<Contact> contacts;
	private Collection<Message> messages;
	
	// the random message generator
	private MessageGenerator messageGen;
	
	/**
	 * Creates a new DummyService
	 */
	public BackupManagerDummy() {
		this.messageGen = new MessageGenerator();
	}
	
	@Override
	public Contact findContact(String contactName, String intlAddress) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Collection<Contact> getContacts() {
		if (contacts == null) {
			contacts = messageGen.generateContacts();
			System.out.println(String.format("Generated %s contacts.", contacts.size()));
		}
		return contacts;
	}

	@Override
	public MessageStore getMessages() {
		if (messages == null) {
			messages = messageGen.generateMessagesFor(contacts);
			System.out.println(String.format("Generated %s messages for %s contacts.", messages.size(), contacts.size()));
		}
		return getMessageStore(messages);
	}

	@Override
	public MessageStore getMessages(Contact c) {
		if (messages == null) {
			messages = messageGen.generateMessagesFor(contacts);
			System.out.println(String.format("Generated %s messages for %s contacts.", messages.size(), contacts.size()));
		}
		return getMessageStore(messages);
	}
	
	/**
	 * Converts a collection to a message store.
	 * @param messages
	 * @return
	 */
	private MessageStore getMessageStore(Collection<Message> messages) {
		MessageStore ms = new MessageStore();
		ms.addAll(messages);
		return ms;
	}

	@Override
	public void importMessages(Collection<Message> messages) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Message importMessage(Message message) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public int getContactCount() {
		return getContacts().size();
	}

	@Override
	public int getMessageCount() {
		return getMessages().size();
	}

}
