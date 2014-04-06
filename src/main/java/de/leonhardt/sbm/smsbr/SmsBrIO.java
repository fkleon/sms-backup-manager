package de.leonhardt.sbm.smsbr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.core.service.MessageIOService;
import de.leonhardt.sbm.smsbr.xml.debug.CustomMarshallListener;
import de.leonhardt.sbm.smsbr.xml.debug.CustomUnmarshallListener;
import de.leonhardt.sbm.smsbr.xml.debug.CustomValidationEventHandler;
import de.leonhardt.sbm.smsbr.xml.model.Sms;
import de.leonhardt.sbm.smsbr.xml.model.Smses;

/**
 * This class is responsible for reading and writing Backup-XML files.
 * 
 * @author Frederik Leonhardt
 *
 */
public class SmsBrIO implements MessageIOService<Sms> {

	private final String XML_XSL_HEADER = "\n<?xml-stylesheet type=\"text/xsl\" href=\"sms.xsl\"?>";
	private final String XML_SCHEMA = "schema/schema.xsd";
	
	protected boolean DEBUG = false;
	protected Logger log = Logger.getLogger("SmsBrIO");
	
	private JAXBContext jc; // our jaxb context
	private Schema schema; // the validation schema, can be null (= no validation)
	
	private Unmarshaller unmarshaller;
	private Marshaller marshaller;
	
	/**
	 * Creates new SmsIO
	 * 
	 * @param includeXSL, if xsl-stylesheet header should be included in XML
	 * @throws MessageIOException, if JAXB can not be initialised
	 */
	public SmsBrIO(boolean includeXSL) throws MessageIOException {
		// load schema
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			URL schemaUrl = this.getClass().getClassLoader().getResource(XML_SCHEMA);
			this.schema = schemaFactory.newSchema(schemaUrl); 
		} catch (SAXException e) {
			log.warning("Schema '" + XML_SCHEMA + "' could not be loaded. No validation will take place.");
		}
		
		// initialize JAXB Context with XML classes
		try {
			this.jc = JAXBContext.newInstance("de.leonhardt.sbm.smsbr.xml");
			// create marshaller and unmarshaller
			this.unmarshaller = jc.createUnmarshaller();
			this.marshaller = jc.createMarshaller();

			// in debug mode, add some verbose output
			if (DEBUG) {
				unmarshaller.setEventHandler(new CustomValidationEventHandler());
				unmarshaller.setListener(new CustomUnmarshallListener());
				marshaller.setEventHandler(new CustomValidationEventHandler());
				marshaller.setListener(new CustomMarshallListener());
			}
		} catch (JAXBException e) {
			throw wrapException(e);
		}
		
		// set schema
		unmarshaller.setSchema(this.schema);
		marshaller.setSchema(this.schema);

		// configure marshaller
		try {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			if (includeXSL) {
				marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", XML_XSL_HEADER); 
			}
		} catch (PropertyException e) {
			throw wrapException(e);
		}
	
		
		// done!
		this.log.fine("Initialized MessageIO (SMS Backup and Restore)."
				+ "\n IncludeXSL = " + includeXSL
				+ "\n Schema = " + (schema == null ? "none" : schema.toString()));
	}
	
	/**
	 * Imports SMS from a given file.
	 * 
	 * @param file the file
	 * @return Messages wrapped by Smses object
	 * 
	 * @throws IllegalArgumentException, if filePath == null
	 * @throws MessageIOException, if file does not exist, does not contain any messages or a JAXB error occured
	 */
	@Override
	public Collection<Sms> readFrom(File file) throws IllegalArgumentException, MessageIOException {
		// check file path
		if (file == null) {
			throw new IllegalArgumentException("File cannot be null!");
		}
		
		// initialize Input Stream
		InputStream is;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw wrapException(e);
		}
		
		Collection<Sms> smsCol = readFrom(is);
		
		log.info("Sucessfully read " + smsCol.size() + " messages from '" + file.getPath() + "'.");
		
		return smsCol;
	}
	
	/**
	 * Imports SMS from a given file path.
	 * 
	 * @param filePath
	 * @return Messages wrapped by Smses object
	 * 
	 * @throws IllegalArgumentException, if filePath == null
	 * @throws MessageIOException, if file does not contain any messages or a JAXB error occured
	 */
	@Override
	public Collection<Sms> readFrom(String filePath) throws IllegalArgumentException, MessageIOException {
		// check file path
		if (filePath == null) {
			throw new IllegalArgumentException("File path cannot be null!");
		}
		
		// initialize File
		File file = new File(filePath);
		
		return readFrom(file);
	}
	
	/**
	 * Imports SMS from a given input stream.
	 * 
	 * @param is the input stream
	 * @return Messages wrapped by Smses object
	 * 
	 * @throws IllegalArgumentException, if inputStream == null
	 * @throws MessageIOException, if stream does not contain any messages or could not be parsed
	 */
	@Override
	public Collection<Sms> readFrom(InputStream inputStream) throws IllegalArgumentException, MessageIOException {
		// check input stream
		if (inputStream == null) {
			throw new IllegalArgumentException("Input stream cannot be null!");
		}
		
		Smses smses;
		try {
			smses = (Smses)unmarshaller.unmarshal(inputStream);
		} catch (JAXBException e) {
			// rethrow
			throw wrapException(e);
		}
		
		// check if import was successful
		if (smses == null || smses.getCount() == null || smses.getSms() == null) {
			// fuck
			log.severe("Import unsuccessful.");
			throw wrapException(new FaultyInputXMLException("Could not parse XML file. Faulty file?"));
		}
		
		// check if number of messages is correct
		Integer expectedCount = smses.getCount();
		Integer actualCount = smses.getSms().size();
		
		if (!expectedCount.equals(actualCount)) {
			log.warning("Expected " + expectedCount + " messages, but found only " + actualCount + " messages.");
		}
		
		log.fine("Sucessfully read " + actualCount + " messages from '" + inputStream.toString() + "'.");
		
		return smses.getSms();
	}
	
	/**
	 * Writes a given Smses object to a given file path. 
	 * @param smses
	 * @param filePath
	 * 
	 * @throws IllegalArgumentException, if filePath is null
	 * @throws JAXBException
	 */
	@Override
	public void writeTo(Collection<Sms> smses, String filePath) throws IllegalArgumentException, MessageIOException {
		// check file path
		if (filePath == null) {
			throw new IllegalArgumentException("File path can not be null!");
		}
		
		// initialize File
		File f = new File(filePath);

		writeTo(smses, f);
	}
	
	/**
	 * Writes a given Smses object to a given file. 
	 * @param smses
	 * @param file
	 * 
	 * @throws IllegalArgumentException, if file is null
	 * @throws JAXBException
	 */
	@Override
	public void writeTo(Collection<Sms> smsCol, File file) throws IllegalArgumentException, MessageIOException {
		// check file
		if (file == null) {
			throw new IllegalArgumentException("File can not be null!");
		}
		
		// create xml wrapper object
		Smses smses = new Smses(smsCol);
		
		// push it out
		try {
			marshaller.marshal(smses,file);
		} catch (JAXBException e) {
			throw wrapException(e);
		}
		
		log.info("Sucessfully wrote " + smses.getCount() + " messages to '" + file.getPath() + "'.");
	}
	
	/**
	 * Wraps the given exception into a MEsssageIOException to conform to the
	 * MessageIOService interface.
	 * 
	 * @param e
	 * @return
	 */
	private MessageIOException wrapException(Exception e) {
		return new MessageIOException(e);
	}
	
}
