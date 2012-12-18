package de.leonhardt.sbm.core.service;

import java.util.Collection;

import de.leonhardt.sbm.core.model.Contact;
import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.core.store.MessageStore;

/**
 * A message service.
 * @author Frederik Leonhardt
 *
 */
public interface MessageService {

	/**
	 * Returns all messages in store.
	 * @return
	 */
	public MessageStore getMessages();
	
	/**
	 * Returns all messages for a given contact.
	 * @param c
	 * @return
	 */
	public MessageStore getMessages(Contact c);
	
	/**
	 * Imports all messages in the given collection,
	 * also assigns IDs to them.
	 * 
	 * @param messages
	 */
	public void importMessages(Collection<Message> messages);
	
	/**
	 * Imports a given message,
	 * also assigns ID to it.
	 * 
	 * @param message
	 * @return reference to the imported message
	 */
	public Message importMessage(Message message);
	
	/**
	 * Returns the total number of messages.
	 * @return
	 */
	public int getMessageCount();
}
