package de.leonhardt.sbm.core.model;

import java.util.Date;

import de.leonhardt.sbm.core.model.MessageConsts.Protocol;
import de.leonhardt.sbm.core.model.MessageConsts.Status;
import de.leonhardt.sbm.core.model.MessageConsts.Type;

/**
 * An internal representation of a message.
 * It contains at least an application-wide unique ID.
 * 
 * Subclasses must implement hashCode and equals for the system
 * to be able to determine duplicates.
 * 
 * @author Frederik Leonhardt
 *
 */
public class Message extends AbstractEntity {
	
	/**
	 * Holds number of duplicates
	 */
	protected int numDuplicates;
	
	// technical information
	protected Protocol protocol;
	protected Type type;
	protected Status status;
	
	// content information
    protected String body;
    protected Date date;
    protected Date dateSent;
    protected Integer read;
    
 	protected String serviceCenter;
    protected String subject;

    // related entity information
    protected Contact contact;
    
    // original information
    protected String originalAddress;
	protected String originalContactName;
    
	public Message(long id) {
		super(id);
		this.numDuplicates = 0;
	}
    
    public int getNumDuplicates() {
    	return this.numDuplicates;
    }
    
    public void setNumDuplicates(int num) {
    	this.numDuplicates = num;
    }
    
    public void incDuplicates() {
    	this.numDuplicates++;
    }
    
    public void incDuplicates(int count) {
    	this.numDuplicates+=count;
    }
    
    public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public Integer getRead() {
		return read;
	}

	public void setRead(Integer read) {
		this.read = read;
	}

	public String getServiceCenter() {
		return serviceCenter;
	}

	public void setServiceCenter(String serviceCenter) {
		this.serviceCenter = serviceCenter;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	public void setOriginalAddress(String address) {
		this.originalAddress = address;
	}
	
	public String getOriginalAddress() {
		return this.originalAddress;
	}
	
	public void setOriginalContactName(String contactName) {
		this.originalContactName = contactName;
	}
	
	public String getOriginalContactName() {
		return this.originalContactName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		return result;
	}

	/**
	 * Used for equals:
	 * - contact
	 * - date
	 * - subject
	 * - type
	 * - body
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Message))
			return false;
		Message other = (Message) obj;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", numDuplicates=" + numDuplicates
				+ ", protocol=" + protocol + ", type=" + type + ", status="
				+ status + ", body=" + body
				+ ", date=" + date + ", dateSent=" + dateSent + ", read="
				+ read + ", serviceCenter=" + serviceCenter + ", subject="
				+ subject + ", contact=" + contact + "]";
	}

}
