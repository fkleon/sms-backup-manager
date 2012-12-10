package de.leonhardt.sbm.gui.service;

import java.util.Collection;

import de.leonhardt.sbm.xml.model.Contact;
import de.leonhardt.sbm.xml.model.Sms;

public interface MessageService {

	//TODO rethink
	public Collection<Sms> getMessages(Contact c);	
}
