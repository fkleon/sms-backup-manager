package de.leonhardt.sbm.core.store;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;

import de.leonhardt.sbm.core.model.Message;

/**
 * The MessageStore holds all messages of a contact.
 * It uses a set and does not store duplicates.
 * However, the original number of duplicates is preserved in the messages itself.
 * 
 * @author Frederik Leonhardt
 *
 */
public class MessageStore extends AbstractCollection<Message> {

	protected Collection<Message> col; // underlying collection
	protected Logger log;
	
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
		this.col = col;
		this.log = Logger.getLogger("MessageStore");
	}

	/**
	 * Adds the given message to store.
	 * If message already exists, increase duplicate count for it.
	 * 
	 * @param msg
	 */
	@Override
	public boolean add(Message msg) {
		for (Message m: this) {
			if (m.equals(msg)) {
				int msgDupes = msg.getNumDuplicates();
				
				// if there are already dupes, add this number instead
				if (msgDupes > 0) {
					// msgDupes + 1 message
					m.incDuplicates(msgDupes+1);
				} else {
					// increase one
					m.incDuplicates();
				}
				//log.info("Throw away duplicate: " + msg + " of " + m);
				return false;
//				System.out.println("contains: " + contains(msg));
//				System.out.println("m: " + m);
//				System.out.println("msg: " + msg);
//				System.out.println("m equals msg: " + m.equals(msg));
//				System.out.println("msg equals m: " + msg.equals(m));
			}
		}
		
		return col.add(msg);
	}
	
	@Override
	public boolean addAll(Collection<? extends Message> msges) {
		boolean hasChanged = false;
		for (Message msg: msges) {
			if(add(msg)) {
				hasChanged = true;
			}
		}
		
		return hasChanged;
	}

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

	@Override
	public Iterator<Message> iterator() {
		return col.iterator();
	}

	@Override
	public int size() {
		return col.size();
	}

}
