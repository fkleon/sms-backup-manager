package de.leonhardt.sbm.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


import de.leonhardt.sbm.util.Utils.IdGenerator;
import de.leonhardt.sbm.xml.model.Sms;

/**
 * The MessageStore holds all messages.
 * 
 * @author Frederik Leonhardt
 *
 */
public class MessageStore extends ObjectStore<Sms> {

	/**
	 * Creates a new MessageStore.
	 * Needs an ID generator to be able to assign ids to messages.
	 * 
	 * @param idGen
	 */
	public MessageStore(IdGenerator idGen) {
		super(idGen);
		this.log = Logger.getLogger("MessageStore");
	}
	
		
	/**
	 * Assigns the next free id to the given message.
	 * 
	 * @param message
	 * @return reference to the original object (now with id)
	 */
	protected Sms assignId(Sms message) {
		message.setId(idGen.getNextId());
		return message;
	}
	
	
	public Map<Sms, Integer> findDuplicates() {
		Map<Sms, Integer> duplicates = new HashMap<Sms, Integer>();
		Set<Sms> tempSet = new HashSet<Sms>();
		
		for (Sms message: col) {
			if (!tempSet.add(message)) {
				// we got ourselves a lil' duplicate here!
				if (duplicates.containsKey(message)) {
					// increment
					int prevValue = duplicates.get(message);
					duplicates.put(message, ++prevValue);
				} else {
					// new entry
					duplicates.put(message, 1);
				}
			}
		}
		return duplicates;
	}
	
	public Collection<Sms> clearDuplicates() {
		Set<Sms> tempSet = new HashSet<Sms>(col);
		log.info("Removed " + (col.size() - tempSet.size()) + " duplicates.");
		return tempSet;
	}

}
