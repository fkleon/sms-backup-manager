package de.leonhardt.sbm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.bind.UnmarshalException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import de.leonhardt.sbm.xml.model.Sms;
import de.leonhardt.sbm.xml.model.Smses;

public class IOTest {

	private static SmsesIO smsIO;
	private static TestUtils testUtils;
	private static String testOutputPath;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		smsIO = new SmsesIO(true);
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
		Smses validSmses = smsIO.readFrom(validPath);
		
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
		} catch (UnmarshalException e) {
			// this should have been caused by a file not found exception
			if (!(e.getCause() instanceof FileNotFoundException))
				throw e;
		}
		
		// null file
		File f = null;
		smsIO.readFrom(f);
	}
	
	@Test
	public void testReadBroken() throws Exception {
		// test reading of broken files
		Smses results = new Smses(new ArrayList<Sms>());
		
		
		String[] invalidPaths = {"fixtures/invalidSms.xml", 	// this file contains invalid entries
								 "fixtures/emptySms.xml", 		// this file contains no message entries
								 "fixtures/invalidSms2.xml", 	// this file contains a message with invalid date
								};
		
		for (int i = 0; i < invalidPaths.length; i++) {
			try {
				results = smsIO.readFrom(invalidPaths[i]);
			} catch (UnmarshalException e) {
				// yep..
				System.out.println(e.toString());
			}
		}

		
		for (Sms s: results.getSms()) {
			System.out.println(s);
		}
	}
	
	@Test
	public void testWriteValidSimple() throws Exception {
		String validPath = "fixtures/sms-20121011143146.xml";
		
		Smses validSmses = smsIO.readFrom(validPath);
		smsIO.writeTo(validSmses, testOutputPath);
		
		int lines1 = testUtils.readLineCount(validPath);
		int lines2 = testUtils.readLineCount(testOutputPath);
		assertTrue("Line count differs.", lines1 == lines2);
	}
	
	@Test
	public void testWriteValidExtended() throws Exception {
		// test if an exported XML contains the same messages as the source XML
		String validPath = "fixtures/sms-20121011143146.xml";
		
		// read original source file
		Smses validSmses = smsIO.readFrom(validPath);
		
		// write bak to temp file
		smsIO.writeTo(validSmses, testOutputPath);
		
		// read temp file
		//String invalidPath = "schema/sms-20121203092757.xml";
		Smses newSmses = smsIO.readFrom(testOutputPath);
		
		// sort first to make equals work
		testUtils.sortSmses(validSmses);
		testUtils.sortSmses(newSmses);
		
		boolean equalMessages = newSmses.getSms() == null ? false : newSmses.getSms().equals(validSmses.getSms());
		
		assertTrue("Messages are not equal.",equalMessages);
	}
	
	public void getRandomSMS() {
		//TODO
		Random rand = new Random(1337);
		
	}
	
}
