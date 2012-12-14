package de.leonhardt.sbm.convert;

import java.util.Collection;

import de.leonhardt.sbm.exception.UnknownProtocolException;
import de.leonhardt.sbm.exception.UnknownStatusException;
import de.leonhardt.sbm.exception.UnknownTypeException;
import de.leonhardt.sbm.model.Message;

/**
 * Interface for a converter between internal and external message formats.
 * 
 * @author Frederik Leonhardt
 *
 * @param <T>
 */
public interface MessageConverter<T> {

	/**
	 * Converts a MessageStore object to a T collection.
	 * 
	 * @param ms
	 * @param keepOriginals, true if original values should be kept.
	 * @param exportDupes, true, if duplicates should be exported.
	 * 
	 * @return
	 */
	public abstract Collection<T> toExternalCol(Collection<Message> ms, boolean keepOriginals,
			boolean exportDupes);

	/**
	 * Converts a T collection to a MessageStore.
	 * Leaves ids unpopulated!
	 * 
	 * @param smses
	 * @return
	 */
	@Deprecated
	public Collection<Message> toInternalCol(Collection<T> smses);
	
	/**
	 * Converts a given T to a Message.
	 * Leaves id unpopulated!
	 *
	 * @param sms
	 * @param contact
	 * @return
	 * @throws UnknownTypeException 
	 * @throws UnknownStatusException 
	 * @throws UnknownProtocolException 
	 * @throws IllegalArgumentException 
	 */
	public abstract Message toInternalMessage(T sms)
			throws IllegalArgumentException, UnknownProtocolException,
			UnknownStatusException, UnknownTypeException;

	/**
	 * Converts a Message to T.
	 * Does not care about duplicates, e.g. only one sms will be returned.
	 * 
	 * @param message
	 * @param keepOriginals, true if original values for address and contact should be kept (non-internationalised).
	 * 
	 * @return
	 */
	public abstract T toExternalMessage(Message message, boolean keepOriginals);

}