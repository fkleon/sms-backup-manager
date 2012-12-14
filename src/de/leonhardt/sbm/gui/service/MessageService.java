package de.leonhardt.sbm.gui.service;

import de.leonhardt.sbm.model.Contact;
import de.leonhardt.sbm.store.MessageStore;

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
}
