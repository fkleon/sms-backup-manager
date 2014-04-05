package de.leonhardt.sbm.core.service;

import java.util.Collection;

import de.leonhardt.sbm.core.model.Contact;

/**
 * A service to operate on contacts.
 * @author Frederik Leonhardt
 *
 */
public interface ContactService {

	/**
	 * Returns the reference to the contact characterized by
	 * the given name and international address.
	 * @param contactName
	 * @param intlAddress
	 * @return null, if no contact found
	 */
	public Contact findContact(String contactName, String intlAddress);
	
	/**
	 * Returns all contacts.
	 * @return
	 */
	public Collection<Contact> getContacts();
	
	/**
	 * Returns the total number of contacts.
	 * @return
	 */
	public int getContactCount();
}
