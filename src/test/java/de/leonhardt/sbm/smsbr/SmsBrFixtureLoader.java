package de.leonhardt.sbm.smsbr;

import java.io.InputStream;
import java.util.Collection;

import de.leonhardt.sbm.TestUtils;
import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.smsbr.xml.model.Sms;

/**
 * Convenience class to load test fixtures from resources.
 * Utilises a {@link SmsBrIO} to load the data.
 * 
 * @author Frederik Leonhardt
 *
 */
public class SmsBrFixtureLoader {

	private SmsBrIO smsIO;
	
	/**
	 * Creates a new fixture loader, which doesn't include header information.
	 * 
	 * @throws MessageIOException
	 */
	public SmsBrFixtureLoader() throws MessageIOException {
		this.smsIO = new SmsBrIO(false);
	}
	
	/**
	 * 
	 * @param resourcePath, absolute with leading /
	 * @return
	 * @throws MessageIOException 
	 * @throws IllegalArgumentException 
	 */
	public Collection<Sms> loadSmses(String resourcePath) throws IllegalArgumentException, MessageIOException {
		InputStream is = TestUtils.getInputStreamForResource(resourcePath);
		return smsIO.readFrom(is);
	}
}
