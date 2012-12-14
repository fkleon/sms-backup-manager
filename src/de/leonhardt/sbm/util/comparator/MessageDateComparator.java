package de.leonhardt.sbm.util.comparator;

import java.util.Comparator;

import de.leonhardt.sbm.model.Message;

/**
 * A comparator implementation for Messages.
 * 
 * Sorts by date and then by contact.
 * 
 * @author Frederik Leonhardt
 *
 */
public class MessageDateComparator implements Comparator<Message> {
		
	@Override
	public int compare(Message o1, Message o2) {
		int compDate = o2.getDate().compareTo(o1.getDate());
		
		if (compDate == 0 && !o1.equals(o2)) {
			return new ContactNameComparator().compare(o1.getContact(), o2.getContact());
		} else {
			return compDate;
		}
	}
}