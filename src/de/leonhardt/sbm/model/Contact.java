package de.leonhardt.sbm.model;

// TODO use contact format from Contact Backup
public class Contact {

	private long id;
	private String contactName;
	private String addressIntl;
	private String countryCode;
	private int numMessages;
		
	public Contact(String contactName, String addressIntl) {
		this.contactName = contactName;
		this.addressIntl = addressIntl;
		this.countryCode = "ZZ";
	}
	
	public Contact(String contactName, String addressIntl, String countryCode) {
		this.contactName = contactName;
		this.addressIntl = addressIntl;
		this.countryCode = countryCode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getAddressIntl() {
		return addressIntl;
	}

	public void setAddressIntl(String addressIntl) {
		this.addressIntl = addressIntl;
	}
	
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public int getNumMessages() {
		return numMessages;
	}

	public void setNumMessages(int numMessages) {
		this.numMessages = numMessages;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", contactName=" + contactName
				+ ", addressIntl=" + addressIntl + ", countryCode=" + countryCode +  "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((addressIntl == null) ? 0 : addressIntl.hashCode());
		result = prime * result
				+ ((contactName == null) ? 0 : contactName.hashCode());
		return result;
	}

	/**
	 * To determine equality, only use:
	 * - (normalized) international address
	 * - contact name
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (addressIntl == null) {
			if (other.addressIntl != null)
				return false;
		} else if (!addressIntl.equals(other.addressIntl))
			return false;
		if (contactName == null) {
			if (other.contactName != null)
				return false;
		} else if (!contactName.equals(other.contactName))
			return false;
		return true;
	}
}
