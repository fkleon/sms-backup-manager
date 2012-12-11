package de.leonhardt.sbm.gui.pm;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

import de.leonhardt.sbm.model.Contact;

/**
 * The Presentation Model of a Contact.
 * Read-only for now.
 * 
 * @author Frederik Leonhardt
 *
 */
public class ContactPM extends AbstractPM {

	public IntegerPM id = new IntegerPM();
	public TextPM contactName = new TextPM();
	public TextPM addressIntl = new TextPM();
	public CountryFlagPM countryFlag = new CountryFlagPM();
	
	// service is only needed for write ops
//	public ContactService contactService;
	
	public ContactPM() {
		PMManager.setup(this);
		
		this.addressIntl.setMandatory(true);
		this.addressIntl.setEditable(false);
		this.id.setEditable(false);
		this.contactName.setEditable(false);
	}
	
	public ContactPM(Contact contact) {
		this();
		init(contact);
	}
	
	private void init(Contact contact) {
		this.id.setInteger((int)contact.getId());
		this.contactName.setText(contact.getContactName());
		this.addressIntl.setText(contact.getAddressIntl());
		this.countryFlag.setCountryCode(contact.getCountryCode());
	}
	
	public void setContact(Contact contact) {
		init(contact);
	}
	
//	@Service
//	public void setMessageService(MessageService service) {
//		this.messageService = service;
//	}
	
}
