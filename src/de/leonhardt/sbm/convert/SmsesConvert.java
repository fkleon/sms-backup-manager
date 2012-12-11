package de.leonhardt.sbm.convert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import de.leonhardt.sbm.exception.UnknownProtocolException;
import de.leonhardt.sbm.exception.UnknownStatusException;
import de.leonhardt.sbm.exception.UnknownTypeException;
import de.leonhardt.sbm.model.Contact;
import de.leonhardt.sbm.model.Message;
import de.leonhardt.sbm.model.MessageConsts.Protocol;
import de.leonhardt.sbm.model.MessageConsts.Status;
import de.leonhardt.sbm.model.MessageConsts.Type;
import de.leonhardt.sbm.store.MessageStore;
import de.leonhardt.sbm.util.Utils;
import de.leonhardt.sbm.util.Utils.IdGenerator;
import de.leonhardt.sbm.xml.model.Sms;
import de.leonhardt.sbm.xml.model.Smses;

/**
 * This class is responsible for conversion between the internal message format
 * and a format used by SMS Backup & Restore.
 * 
 * @author Frederik Leonhardt
 *
 */
public class SmsesConvert {

	private static SmsesConvert _instance;
	
	private IdGenerator idGen;
	// Oct 21, 2011 9:50:00 AM
	private SimpleDateFormat sdf;
	
	private SmsesConvert() {
		this.idGen = Utils.getDefaultIdGenerator();
		this.sdf = new SimpleDateFormat("MMM dd, yyyy, K:mm:ss a", Locale.US);
	}
	
	/**
	 * Returns an instance of the SmsesConvert Utility.
	 * @return
	 */
	public static SmsesConvert getInstance() {
		if (_instance == null) {
			_instance = new SmsesConvert();
		}
		return _instance;
	}
	
	/**
	 * Converts a MessageStore object to Smses.
	 * 
	 * @param ms
	 * @param keepOriginals, true if original values should be kept.
	 * @param exportDupes, true, if duplicates should be exported.
	 * 
	 * @return
	 */
	public Smses convert(MessageStore ms, boolean keepOriginals, boolean exportDupes) {
		List<Sms> smsList = new ArrayList<Sms>();
		for (Message m: ms) {
			int exportCount = exportDupes ? m.getNumDuplicates()+1 : 1; // numDuplicates + 1 export
			
			for (int i = 0; i < exportCount; i++) { 
				smsList.add(toSms(m,keepOriginals));
			}
		}
		return new Smses(smsList);
	}
	
	/**
	 * Convert a Smses object to a MessageStore.
	 * Does not populate contact information!
	 * 
	 * @param smses
	 * @return
	 */
	protected MessageStore convert(Smses smses) {
		MessageStore ms = new MessageStore();
		
		for (Sms sms: smses.getSms()) {
			try {
				ms.add(toMessage(sms));
			} catch (Exception e) {
				Logger.getAnonymousLogger().warning("Could not convert SMS to Message: "+e.toString());
			}
		}
		
		return ms;
	}
	
	/**
	 * Converts a given Sms to a Message.
	 * Additionally assigns given contact.
	 *  
	 * @param sms
	 * @param contact
	 * @return
	 * @throws UnknownTypeException 
	 * @throws UnknownStatusException 
	 * @throws UnknownProtocolException 
	 * @throws IllegalArgumentException 
	 */
	public Message toMessage(Sms sms, Contact contact) throws IllegalArgumentException, UnknownProtocolException, UnknownStatusException, UnknownTypeException {
		Message message = toMessage(sms);
		
		message.setContact(contact);
		
		return message;
	}
	
	/**
	 * Converts a given Sms to a Message.
	 * Leaves address and contact unpopulated!
	 * 
	 * @param sms
	 * @return
	 * @throws IllegalArgumentException
	 * @throws UnknownProtocolException
	 * @throws UnknownStatusException
	 * @throws UnknownTypeException
	 */
	protected Message toMessage(Sms sms) throws IllegalArgumentException, UnknownProtocolException, UnknownStatusException, UnknownTypeException {
		Message message = new Message(idGen.getNextId());
		
		message.setBody(sms.getBody());
		//message.setContact(n/a);
		message.setDate(getDateOrNull(sms.getDate()));
		message.setDateSent(getDateOrNull(sms.getDateSent()));
		message.setProtocol(Protocol.toProtocol(ensureNotNull(sms.getProtocol())));
		message.setRead(sms.getRead());
		message.setServiceCenter(sms.getServiceCenter());
		message.setStatus(Status.toStatus(ensureNotNull(sms.getStatus())));
		message.setSubject(sms.getSubject());
		message.setType(Type.toType(ensureNotNull(sms.getType())));
		message.setOriginalAddress(sms.getAddress());
		message.setOriginalContactName(sms.getContactName());
		
		return message;
	}
	
	/**
	 * Converts a Message to Sms.
	 * Does not care about duplicates, e.g. only one sms will be returned.
	 * 
	 * @param message
	 * @param keepOriginals, true if original values for address and contact should be kept (non-internationalised).
	 * 
	 * @return
	 */
	public Sms toSms(Message message, boolean keepOriginals) {
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
