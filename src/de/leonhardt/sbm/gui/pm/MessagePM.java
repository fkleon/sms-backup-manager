package de.leonhardt.sbm.gui.pm;

import java.util.Date;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

import de.leonhardt.sbm.xml.model.Sms;

/**
 * PresenterModel of a message.
 * 
 * @author Frederik Leonhardt
 *
 */
public class MessagePM extends AbstractPM {

	public TextPM body = new TextPM();
	public DatePM date = new DatePM();
	public TextPM address = new TextPM();
	public IntegerPM type = new IntegerPM();
	public IntegerPM status = new IntegerPM();
	
	public MessagePM() {
		PMManager.setup(this);
		
		body.setEditable(false);
		date.setEditable(false);
		address.setEditable(false);
		type.setEditable(false);
		status.setEditable(false);
	}
	
	public MessagePM(Sms sms) {
		this();
		init(sms);
	}
	
	private void init(Sms sms) {
		this.body.setText(sms.getBody());
		this.date.setDate(new Date(sms.getDate()));
		this.address.setText(sms.getAddress());
		this.type.setInteger(sms.getType());
		this.status.setInteger(sms.getStatus());
	}
		
	public void setMessage(Sms sms) {
		init(sms);
	}
	
}
