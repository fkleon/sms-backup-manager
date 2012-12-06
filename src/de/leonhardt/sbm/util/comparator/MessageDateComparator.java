package de.leonhardt.sbm.util.comparator;

import java.util.Comparator;

import de.leonhardt.sbm.xml.model.Sms;

public class MessageDateComparator implements Comparator<Sms> {

	private boolean desc;
	
	public MessageDateComparator() {
		this.desc = false;
	}
	
	public MessageDateComparator(boolean desc) {
		this.desc = desc;
	}
	
	@Override
	public int compare(Sms o1, Sms o2) {
		if (desc) {
			return o2.getDate().compareTo(o1.getDate());
		} else {
			return o1.getDate().compareTo(o2.getDate());
		}
	}
}