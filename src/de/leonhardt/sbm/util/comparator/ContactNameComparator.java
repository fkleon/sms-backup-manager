package de.leonhardt.sbm.util.comparator;

import java.util.Comparator;

import de.leonhardt.sbm.model.Contact;

public class ContactNameComparator implements Comparator<Contact> {
	@Override
	public int compare(Contact o1, Contact o2) {
		// compare name
		int compName = o1.getContactName().compareTo(o2.getContactName());
		
		if (compName == 0 && !o1.equals(o2)) {
			// compare number
			return o1.getAddressIntl().compareTo(o2.getAddressIntl());
		} else {
			return compName;
		}
	}
}