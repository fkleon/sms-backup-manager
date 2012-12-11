package de.leonhardt.sbm.gui.service;

import de.leonhardt.sbm.model.Contact;
import de.leonhardt.sbm.store.MessageStore;

public interface MessageService {

	//TODO rethink
	public MessageStore getMessages();
	public MessageStore getMessages(Contact c);
}
