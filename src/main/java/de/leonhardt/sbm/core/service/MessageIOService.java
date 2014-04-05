package de.leonhardt.sbm.core.service;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.smsbr.xml.model.Sms;

/**
 * Message I/O interface.
 * 
 * @author Frederik Leonhardt
 *
 * @param <E>
 */
public interface MessageIOService<E> {

	/**
	 * Reads from a given file path.
	 * 
	 * @param filePath
	 * @return
	 * @throws IllegalArgumentException
	 * @throws MessageIOException
	 */
	public Collection<E> readFrom(String filePath) throws IllegalArgumentException, MessageIOException;
	
	/**
	 * Reads from a given file.
	 * 
	 * @param file
	 * @return
	 * @throws IllegalArgumentException
	 * @throws MessageIOException
	 */
	public Collection<E> readFrom(File file) throws IllegalArgumentException, MessageIOException;
	
	/**
	 * Imports SMS from a given input stream.
	 * 
	 * @param is the input stream
	 * @return Messages wrapped by Smses object
	 * 
	 * @throws IllegalArgumentException, if inputStream == null
	 * @throws MessageIOException, if stream does not contain any messages or could not be parsed
	 */
	public Collection<Sms> readFrom(InputStream inputStream) throws IllegalArgumentException, MessageIOException;
	
	/**
	 * Writes to a given file path.
	 * 
	 * @param objs
	 * @param filePath
	 * @throws IllegalArgumentException
	 * @throws MessageIOException
	 */
	public void writeTo(Collection<E> objs, String filePath) throws IllegalArgumentException, MessageIOException;
	
	/**
	 * Writes to a given file.
	 * 
	 * @param objs
	 * @param file
	 * @throws IllegalArgumentException
	 * @throws MessageIOException
	 */
	public void writeTo(Collection<E> objs, File file) throws IllegalArgumentException, MessageIOException;	
}
