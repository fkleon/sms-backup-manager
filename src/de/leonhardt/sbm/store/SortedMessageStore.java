package de.leonhardt.sbm.store;

import java.util.Comparator;
import java.util.TreeSet;

import de.leonhardt.sbm.model.Message;
import de.leonhardt.sbm.util.comparator.MessageDateComparator;

/**
 * A sorted message store.
 * Guarantees a certain sorting order.
 * 
 * @author Frederik Leonhardt
 *
 */
public class SortedMessageStore extends MessageStore {
	
	final static Comparator<Message> dateComp = new MessageDateComparator();

	/**
	 * Creates a new SortedMessageStore.
	 * Sorting order is message date, then contact name, then contact number.
	 */
	public SortedMessageStore() {
		this(dateComp);
	}
	
	/**
	 * Creates a new SortedMessgeStore with given comparator.
	 * @param comp
	 */
	public SortedMessageStore(Comparator<Message> comp) {
		super(new TreeSet<Message>(comp));
	}

}
