package de.leonhardt.sbm.smsbr;

import java.io.InputStream;
import java.util.Collection;

import de.leonhardt.sbm.TestUtils;
import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.smsbr.xml.model.Sms;
import de.leonhardt.sbm.smsbr.xml.model.Smses;

public class SmsBrFixtureLoader {

	private SmsBrIO smsIO;
	
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
