package de.leonhardt.sbm.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import de.leonhardt.sbm.TestUtils;
import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.smsbr.SmsBrIO;
import de.leonhardt.sbm.smsbr.xml.model.Sms;
import de.leonhardt.sbm.smsbr.xml.model.Smses;

public class IOTest {

	private static SmsBrIO smsIO;
	private static TestUtils testUtils;
	private static String testOutputPath;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		smsIO = new SmsBrIO(true);
		testUtils = new TestUtils();
		testOutputPath = "test.xml";
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Removing temporary test files..");
		new File(testOutputPath).delete();
		System.out.println("Done!");
	}
	
	@Test
	public void testReadValid() throws Exception {
		// test, if a file can be read successfully
		String validPath = "fixtures/sms-20121011143146.xml";
		int messageCount = 2444;
		Smses validSmses = new Smses(smsIO.readFrom(validPath));
		
		assertNotNull("The imported object is null.", validSmses);
		assertNotNull("The sms list is null.", validSmses.getSms());
		assertNotNull("The sms count is null.", validSmses.getCount());
		assertEquals("Count and number of messages differ.", (int)validSmses.getCount(), validSmses.getSms().size());
		assertEquals(messageCount, (int)validSmses.getCount());
		assertEquals(messageCount, validSmses.getSms().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReadNotExistent() throws Exception {
		// this file does not exist
		String invalidPath = "fixtures/whatup.xml";
		try {
			smsIO.readFrom(invalidPath);
		} catch (MessageIOException e) {
			// this should have been caused by a file not found exception
			if (!(e.toString().contains("FileNotFound")))
				throw e;
		}
		
		// null file
		File f = null;
		smsIO.readFrom(f);
	}
	
	@Test
	public void testReadBroken() throws Exception {
		// test reading of broken files
		Collection<Sms> results = new ArrayList<Sms>();
		
		String[] invalidPaths = {"fixtures/invalidSms.xml", 	// this file contains invalid entries
								 "fixtures/emptySms.xml", 		// this file contains no message entries
								 "fixtures/invalidSms2.xml", 	// this file contains a message with invalid date
								};
		
		for (int i = 0; i < invalidPaths.length; i++) {
			try {
				results = smsIO.readFrom(invalidPaths[i]);
			} catch (MessageIOException e) {
				// yep..
				System.out.println(e.toString());
			}
		}

		
		for (Sms s: results) {
			System.out.println(s);
		}
	}
	
	@Test
	public void testWriteValidSimple() throws Exception {
		String validPath = "fixtures/sms-20121011143146.xml";
		
		Smses validSmses = new Smses(smsIO.readFrom(validPath));
		smsIO.writeTo(validSmses.getSms(), testOutputPath);
		
		int lines1 = testUtils.readLineCount(validPath);
		int lines2 = testUtils.readLineCount(testOutputPath);
		assertTrue("Line count differs.", lines1 == lines2);
	}
	
	@Test
	public void testWriteValidExtended() throws Exception {
		// test if an exported XML contains the same messages as the source XML
		String validPath = "fixtures/sms-20121011143146.xml";
		
		// read original source file
		Collection<Sms> validSmses = smsIO.readFrom(validPath);
		
		// write bak to temp file
		smsIO.writeTo(validSmses, testOutputPath);
		
		// read temp file
		//String invalidPath = "schema/sms-20121203092757.xml";
		Collection<Sms> newSmses = smsIO.readFrom(testOutputPath);
		
		// sort first to make equals work
		testUtils.sortSmses(new Smses(validSmses));
		testUtils.sortSmses(new Smses(newSmses));
		
		boolean equalMessages = newSmses == null ? false : newSmses.equals(validSmses);
		
		assertTrue("Messages are not equal.",equalMessages);
	}
	
	public void getRandomSMS() {
		//TODO
		//Random rand = new Random(1337);
	}
	
}
