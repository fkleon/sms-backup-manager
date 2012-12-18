package de.leonhardt.sbm.smsbr;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import de.leonhardt.sbm.core.AbstractConverter;
import de.leonhardt.sbm.core.PhoneNumberParser;
import de.leonhardt.sbm.core.exception.UnknownProtocolException;
import de.leonhardt.sbm.core.exception.UnknownStatusException;
import de.leonhardt.sbm.core.exception.UnknownTypeException;
import de.leonhardt.sbm.core.model.Contact;
import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.core.model.MessageConsts.Protocol;
import de.leonhardt.sbm.core.model.MessageConsts.Status;
import de.leonhardt.sbm.core.model.MessageConsts.Type;
import de.leonhardt.sbm.core.store.MessageStore;
import de.leonhardt.sbm.smsbr.xml.model.Sms;

/**
 * This class is responsible for conversion between the internal message format
 * and a format used by SMS Backup & Restore.
 * 
 * @author Frederik Leonhardt
 *
 */
public class SmsBrConverter extends AbstractConverter<Sms> {

	// Oct 21, 2011 9:50:00 AM
	private SimpleDateFormat sdf;
	
	/**
	 * Creates new SMS Backup & Restore Converter.
	 * Initializes a Phone Number Parser.
	 */
	public SmsBrConverter() {
		super(new PhoneNumberParser());
		this.sdf = new SimpleDateFormat("MMM dd, yyyy, K:mm:ss a", Locale.US);
	}
	
	@Override
	public MessageStore toInternalCol(Collection<Sms> smses) {
		MessageStore ms = new MessageStore();
		
		// This will already filter out dupes.
		for (Sms sms: smses) {
			try {
				ms.add(toInternalMessage(sms));
			} catch (Exception e) {
				Logger.getAnonymousLogger().warning("Could not convert SMS to Message: "+e.toString());
			}
		}
		
		return ms;
	}

	@Override
	public Message toInternalMessage(Sms sms)
			throws IllegalArgumentException, UnknownProtocolException,
			UnknownStatusException, UnknownTypeException {
		Message message = new Message(-1); // set id later
		
		// determine type of message
		Type type = Type.toType(ensureNotNull(sms.getType()));
		
		// internationalize contact / address
		Contact c = getNormalizedContact(sms.getContactName(), sms.getAddress());
		
		message.setContact(c);
		message.setType(type);
		message.setBody(sms.getBody());
		message.setDate(getDateOrNull(sms.getDate()));
		
		// if message type is not SENT, do not set the date sent!
		// this would result in duplicate messages on the phone later.
		message.setDateSent((type != Type.Sent) ? null : getDateOrNull(sms.getDateSent()));
		
		message.setProtocol(Protocol.toProtocol(ensureNotNull(sms.getProtocol())));
		message.setRead(sms.getRead());
		message.setServiceCenter(sms.getServiceCenter());
		message.setStatus(Status.toStatus(ensureNotNull(sms.getStatus())));
		message.setSubject(sms.getSubject());
		
		// keep original values
		message.setOriginalAddress(sms.getAddress());
		message.setOriginalContactName(sms.getContactName());
		
		return message;
	}

	@Override
	public Sms toExternalMessage(Message message, boolean keepOriginals) {
		Sms sms = toSms(message);
		
		if (keepOriginals) {
			sms.setAddress(message.getOriginalAddress());
			sms.setContactName(message.getOriginalContactName());
		}
		
		return sms;
	}
	
	/**
	 * Converts a Message to Sms.
	 * Overwrites old address and contact name (international format).
	 * 
	 * @param message
	 * @return
	 */
	protected Sms toSms(Message message) {
		Sms sms = new Sms();
		
		sms.setAddress(message.getContact().getAddressIntl());
		sms.setBody(message.getBody());
		sms.setContactName(message.getContact().getContactName());
		sms.setDate(message.getDate().getTime());
		
		// This validation already takes place while importing messages.
		/*
		if (message.getType() == Type.Received && message.getDateSent() != null) {
			sms.setDateSent(null);
			System.out.println("date information wrong.");
		} else if (message.getType() == Type.Sent) {
			sms.setDateSent(getTimeOrNull(message.getDateSent()));
		} else {
			System.out.println("help " + message);
		}
		*/
		
		sms.setDateSent(getTimeOrNull(message.getDateSent()));
		sms.setLocked(0); // TODO check
		sms.setProtocol(message.getProtocol().getValue());
		sms.setRead(message.getRead());
		sms.setReadableDate(sdf.format(message.getDate()));
		sms.setScToa("null");
		sms.setServiceCenter(message.getServiceCenter());
		sms.setStatus(message.getStatus().getValue());
		sms.setSubject(message.getSubject());
		sms.setToa("null");
		sms.setType(message.getType().getValue());
		
		return sms;
	}

	private Date getDateOrNull(Long time) {
		return (time==null?null:new Date(time));
	}
	
	private Long getTimeOrNull(Date date) {
		return (date==null?null:date.getTime());
	}
		
	private <E> E ensureNotNull(E o) throws IllegalArgumentException {
		if (o == null) throw new IllegalArgumentException("Object can not be null");
		return o;
	}
	
}
