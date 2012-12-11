package de.leonhardt.sbm.store;

import java.util.Comparator;
import java.util.TreeSet;

import de.leonhardt.sbm.model.Message;
import de.leonhardt.sbm.util.Utils.IdGenerator;
import de.leonhardt.sbm.util.comparator.MessageDateComparator;

/**
 * A sorted message store.
 * Guarantees sorting order.
 * 
 * @author Frederik Leonhardt
 *
 */
public class SortedMessageStore extends MessageStore {
	
	final static Comparator<Message> dateComp = new MessageDateComparator();

	public SortedMessageStore() {
		this(dateComp);
	}
	
	public SortedMessageStore(Comparator<Message> comp) {
		super(new TreeSet<Message>(comp));
	}

}
