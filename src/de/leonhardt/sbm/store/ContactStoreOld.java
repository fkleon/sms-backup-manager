package de.leonhardt.sbm.store;

import java.util.HashSet;
import java.util.logging.Logger;


import de.leonhardt.sbm.util.Utils.IdGenerator;
import de.leonhardt.sbm.xml.model.Contact;

public class ContactStoreOld extends ObjectStoreOld<Contact> {

	/**
	 * Creates a new ContactStore.
	 * Needs an ID generator to be able to assign ids to contacts.
	 * 
	 * @param idGen
	 */
	public ContactStoreOld(IdGenerator idGen) {
		super(idGen);
		this.col = new HashSet<Contact>();
		this.log = Logger.getLogger("ContactStore");
	}
	
	/**
	 * Assigns the next free id to the given contact.
	 * 
	 * @param contact
	 * @return reference to the original object (now with id)
	 */
	protected Contact assignId(Contact contact) {
		contact.setId(idGen.getNextId());
		return contact;
	}

}
