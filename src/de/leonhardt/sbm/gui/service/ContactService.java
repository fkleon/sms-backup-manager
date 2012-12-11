package de.leonhardt.sbm.gui.service;

import java.util.Collection;

import de.leonhardt.sbm.model.Contact;

public interface ContactService {

	public Contact findContact(String contactName, String intlAddress);
	
	public Collection<Contact> getContacts();
}
