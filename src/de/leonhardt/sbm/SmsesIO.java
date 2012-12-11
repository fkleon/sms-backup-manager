package de.leonhardt.sbm;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import de.leonhardt.sbm.exception.FaultyInputXMLException;
import de.leonhardt.sbm.xml.debug.CustomMarshallListener;
import de.leonhardt.sbm.xml.debug.CustomUnmarshallListener;
import de.leonhardt.sbm.xml.debug.CustomValidationEventHandler;
import de.leonhardt.sbm.xml.model.Smses;

/**
 * This class is responsible for reading and writing Backup-XML files.
 * 
 * @author Frederik Leonhardt
 *
 */
public class SmsesIO {

	private final String XML_XSL_HEADER = "\n<?xml-stylesheet type=\"text/xsl\" href=\"sms.xsl\"?>";
	private final String XML_SCHEMA = "schema/schema.xsd";
	protected boolean DEBUG = false;
	protected Logger log = Logger.getLogger("SmsIO");
	
	private JAXBContext jc; // our jaxb context
	private Schema schema; // the validation schema, can be null (= no validation)
	private boolean includeXSL; // do we include XSL header?
	
	/**
	 * Creates new SmsIO
	 * 
	 * @param includeXSL, if xsl-stylesheet header should be included in XML
	 * @throws JAXBException, if JAXB can not be initialized
	 */
	public SmsesIO(boolean includeXSL) throws JAXBException {
		// do we include XSL header information?
		this.includeXSL = includeXSL;
		
		// initialize JAXB Context with XML classes
		this.jc = JAXBContext.newInstance("de.leonhardt.sbm.xml");
		
		// load schema
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			this.schema = schemaFactory.newSchema(new File(XML_SCHEMA)); 
		} catch (Exception e) {
			log.warning("Schema '" + XML_SCHEMA + "' could not be loaded. No validation will take place.");
			e.printStackTrace();
		}
		
		// done!
		this.log.info("Initialized MessageIO."
				+ "\n IncludeXSL = " + includeXSL
				+ "\n Schema = " + schema);
	}
	
	/**
	 * Imports SMS from a given file path.
	 * 
	 * @param filePath
	 * @return Messages wrapped by Smses object
	 * 
	 * @throws IllegalArgumentException, if filePath == null
	 * @throws FaultyInputXMLException, if file does not contain any messages
	 * @throws JAXBException
	 */
	public Smses readFrom(String filePath) throws IllegalArgumentException, FaultyInputXMLException, JAXBException {
		// check file path
		if (filePath == null) {
			throw new IllegalArgumentException("File path can not be null!");
		}
		
		// initialize File
		File file = new File(filePath);
		
		return readFrom(file);
	}
	
	/**
	 * Imports SMS from a given file.
	 * 
	 * @param file
	 * @return Messages wrapped by Smses object
	 * 
	 * @throws IllegalArgumentException, if file == null
	 * @throws FaultyInputXMLException, if file does not contain any messages
	 * @throws JAXBException
	 */
	public Smses readFrom(File file) throws IllegalArgumentException, FaultyInputXMLException, JAXBException {
		// check file
		if (file == null) {
			throw new IllegalArgumentException("File can not be null!");
		}
		
		// create unmarshaller
		Unmarshaller unmarshaller = this.jc.createUnmarshaller();
		unmarshaller.setSchema(this.schema);

		// in debug mode, add some verbose output
		if (DEBUG) {
			unmarshaller.setEventHandler(new CustomValidationEventHandler());
			unmarshaller.setListener(new CustomUnmarshallListener());
		}
		
		Smses smses = (Smses)unmarshaller.unmarshal(file);
		
		// check if import was successful
		if (smses == null || smses.getCount() == null || smses.getSms() == null) {
			// fuck
			log.severe("Import unsuccessful.");
			throw new FaultyInputXMLException("Could not parse XML file. Faulty file?");
		}
		
		// check if number of messages is correct
		Integer expectedCount = smses.getCount();
		Integer actualCount = smses.getSms().size();
		
		if (!expectedCount.equals(actualCount)) {
			log.warning("Expected " + expectedCount + " messages, but found only " + actualCount + " messages.");
		}
		
		log.info("Sucessfully read " + actualCount + " messages from '" + file.getPath() + "'.");
		
		return smses;
	}
	
	/**
	 * Writes a given Smses object to a given file path. 
	 * @param smses
	 * @param filePath
	 * 
	 * @throws IllegalArgumentException, if filePath is null
	 * @throws JAXBException
	 */
	public void writeTo(Smses smses, String filePath) throws IllegalArgumentException, JAXBException {
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
	public void writeTo(Smses smses, File file) throws IllegalArgumentException, JAXBException {
		// check file
		if (file == null) {
			throw new IllegalArgumentException("File can not be null!");
		}
		
		// create marshaller
		Marshaller marshaller = this.jc.createMarshaller();
		marshaller.setSchema(this.schema);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		if (includeXSL) {
			marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", XML_XSL_HEADER); 
		}
		
		if (DEBUG) {
			marshaller.setEventHandler(new CustomValidationEventHandler());
			marshaller.setListener(new CustomMarshallListener());
		}
		
		marshaller.marshal(smses,file);
		
		log.info("Sucessfully wrote " + smses.getCount() + " messages to '" + file.getPath() + "'.");
	}
	
}
