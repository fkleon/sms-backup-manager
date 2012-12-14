package de.leonhardt.sbm.convert;

import java.util.ArrayList;
import java.util.Collection;

import de.leonhardt.sbm.PhoneNumberParser;
import de.leonhardt.sbm.exception.UnknownProtocolException;
import de.leonhardt.sbm.exception.UnknownStatusException;
import de.leonhardt.sbm.exception.UnknownTypeException;
import de.leonhardt.sbm.model.Contact;
import de.leonhardt.sbm.model.Message;

/**
 * Any converter should extend AbstractConverter.
 * 
 * @author Frederik Leonhardt
 *
 * @param <T>
 */
public abstract class AbstractConverter<T> implements MessageConverter<T> {

	// the pnp used for contact and phone number normalisation
	protected PhoneNumberParser pnp;
	
	/**
	 * Creates a new converter with given PNP.
	 * @param pnp
	 */
	public AbstractConverter(PhoneNumberParser pnp) {
		this.pnp = pnp;
	}
	
	@Override
	public Collection<T> toExternalCol(Collection<Message> ms, boolean keepOriginals,
			boolean exportDupes) {
		Collection<T> extMsgs = new ArrayList<T>();
		for (Message msg: ms) {
			int exportCount = exportDupes ? msg.getNumDuplicates()+1 : 1; // numDuplicates + 1 export
			
			for (int i = 0; i < exportCount; i++) { 
				extMsgs.add(toExternalMessage(msg, keepOriginals));
			}
		}
		
		return extMsgs;
	}

	@Override
	public abstract Collection<Message> toInternalCol(Collection<T> smses);
	/*{
		MessageStore ms = new MessageStore();
		
		for (T extMsg: smses) {
			Message msg = toInternalMessage(extMsg);
			ms.add(msg);
		}
		
		return ms;
	}*/

	@Override
	public abstract Message toInternalMessage(T sms) throws IllegalArgumentException,
			UnknownProtocolException, UnknownStatusException,
			UnknownTypeException;

	@Override
	public abstract T toExternalMessage(Message message, boolean keepOriginals);
	
	/**
	 * Build a contact object from given name and address.
	 * - Normalises name by trimming
	 * - Normalises address by conversion to international format
	 * - Adds country code 
	 * 
	 * @param name
	 * @param address
	 * @return
	 */
	protected Contact getNormalizedContact(String name, String address) {
		String contactName = name.trim();
		String addressIntl = pnp.getInternationalFormat(address);
		String countryCode = pnp.getCountryCode(address);
		Contact contact = new Contact(contactName, addressIntl, countryCode);
		return contact;
	}

}
