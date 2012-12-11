package de.leonhardt.sbm.store;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

import de.leonhardt.sbm.model.Message;
import de.leonhardt.sbm.util.Utils.IdGenerator;
import de.leonhardt.sbm.xml.model.Smses;

/**
 * The MessageStore holds all messages of a contact.
 * It uses a set and does not store duplicates.
 * However, the original number of duplicates is preserved in the messages itself.
 * 
 * @author Frederik Leonhardt
 *
 */
public class MessageStore extends ObjectStore<Message> {

	/**
	 * Creates a new MessageStore.
	 * Needs an ID generator to be able to assign ids to messages.
	 * 
	 * @param idGen
	 */
	public MessageStore() {
		this(new HashSet<Message>());
	}
	
	/**
	 * Creates a new MessageStore.
	 * Needs an ID generator to be able to assign ids to messages.
	 * Needs collection to use.
	 * 
	 * @param idGen
	 */
	protected MessageStore(Collection<Message> col) {
		super(col);
		this.log = Logger.getLogger("MessageStore");
	}
	
		
	/**
	 * Assigns the next free id to the given message,
	 * if it has no id yet.
	 * 
	 * @param message
	 * @return reference to the original object (now with id)
	 */
	/*
	protected Message assignId(Message message) {
		if (message.getId() <= 0) {
			message.setId(idGen.getNextId());
		}
		return message;
	}
	*/

	@Override
	public boolean add(Message msg) {
		for (Message m: this) {
			if (m.equals(msg)) {
				m.incDuplicates();
				log.info("Throw away duplicate: " + msg + " of " + m);
				return false;
//				System.out.println("contains: " + contains(msg));
//				System.out.println("m: " + m);
//				System.out.println("msg: " + msg);
//				System.out.println("m equals msg: " + m.equals(msg));
//				System.out.println("msg equals m: " + msg.equals(m));
			}
		}
		
		return col.add(msg);
		/*
		if (!addObject(msg)) {
			find(msg).incDuplicates();
			return false;
		} else {
			return true;
		}*/
	}
	
	/*@Override
	public boolean addAll(Collection<? extends Message> msges) {
		boolean hasChanged = false;
		for (Message msg: msges) {
			if(add(msg)) {
				hasChanged = true;
			}
		}
		
		return hasChanged;
	}
*/
	/**
	 * Returns total number of duplicates in this storage.
	 * 
	 * @return
	 */
	public int countDuplicates() {
		int duplicates = 0;
		
		for (Message m: this) {
			duplicates += m.getNumDuplicates();
		}
		
		return duplicates;
	}
	
//	public Smses toSmses() {
//		//Smses smses = new Smses(this);
//	}
	
	/*
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
	*/
	
	/*
	public Collection<Message> clearDuplicates() {
		Set<Message> tempSet = new HashSet<Message>(col);
		log.info("Removed " + (col.size() - tempSet.size()) + " duplicates.");
		return tempSet;
	}
	*/

}
