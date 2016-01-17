package de.leonhardt.sbm.smsbr;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import de.leonhardt.sbm.TestUtils;
import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.core.service.MessageIOService;
import de.leonhardt.sbm.smsbr.SmsBrIO;
import de.leonhardt.sbm.smsbr.xml.model.Sms;
import de.leonhardt.sbm.smsbr.xml.model.Smses;

/**
 * Tests the {@link MessageIOService} implementation {@link SmsBrIO}.
 *
 * @author Frederik Leonhardt
 *
 */
public class SmsBrIOTest {

	private MessageIOService<Sms> smsIO;
	private static TestUtils testUtils;
	private String testOutputPath;

	private static final String RES_SMS_DUPES = "/fixtures/sms-dupes.xml";
	private static final String RES_SMS_NON_DUPES = "/fixtures/sms-non-dupes.xml";
	private static final String RES_SMS_NON_DUPES_2 = "/fixtures/sms-non-dupes-2.xml";
	private static final String RES_SMS_AND_MMS = "/fixtures/sms-and-mms.xml";
	private static final String RES_NOT_EXISTENT = "/fixtures/whatup.xml";
	private static final String[] RES_SMS_INVALIDS =
			{"/fixtures/invalidSms.xml", 	// this file contains invalid entries
			 "/fixtures/emptySms.xml", 		// this file contains no message entries
			 "/fixtures/invalidSms2.xml", 	// this file contains a message with invalid date
			};

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testUtils = new TestUtils();
	}

	/**
	 * SmsBr is not thread safe, create new object for each test
	 * to allow multi-threaded test executions.
	 *
	 * @throws Exception
	 */
	@Before
	public void setUpBeforeTests() throws Exception {
		smsIO = new SmsBrIO(false);
		testOutputPath = "test-" + UUID.randomUUID().toString() + ".xml";
	}

	@After
	public void afterTests() throws Exception {
		System.out.println("Removing temporary test file " + testOutputPath);
		File f = new File(testOutputPath);
		if(f.exists()) f.delete();
	}


	/**
	 * Reads a valid XML file.
	 *
	 * @throws Exception
	 */
	@Test
	public void testReadValid() throws Exception {
		// test, if a file can be read successfully
		//String validPath = "fixtures/sms-20121011143146.xml";
		//int messageCount = 2444;
		int messageCount = 8;

		InputStream is = TestUtils.getInputStreamForResource(RES_SMS_DUPES);
		Smses validSmses = new Smses(smsIO.readFrom(is));

		assertNotNull("The imported object is null.", validSmses);
		assertNotNull("The sms list is null.", validSmses.getSms());
		assertNotNull("The sms count is null.", validSmses.getCount());
		assertEquals("Count and number of messages differ, expected " + validSmses.getCount() + " but was" + validSmses.getSms().size(),
				(int)validSmses.getCount(), validSmses.getSms().size());
		assertEquals(messageCount, (int)validSmses.getCount());
		assertEquals(messageCount, validSmses.getSms().size());
	}

	/**
	 * Reads a valid XML file with MMS.
	 *
	 * @throws Exception
	 */
	@Test
	public void testReadValidWithMms() throws Exception {
		// test, if a file can be read successfully
		int messageCount = 1; //TODO should be 2 with MMS

		InputStream is = TestUtils.getInputStreamForResource(RES_SMS_AND_MMS);
		Smses validSmses = new Smses(smsIO.readFrom(is));

		assertNotNull("The imported object is null.", validSmses);
		assertNotNull("The sms list is null.", validSmses.getSms());
		assertNotNull("The sms count is null.", validSmses.getCount());
		assertEquals("Count and number of messages differ, expected " + validSmses.getCount() + " but was" + validSmses.getSms().size(),
				(int)validSmses.getCount(), validSmses.getSms().size());
		assertEquals(messageCount, (int)validSmses.getCount());
		assertEquals(messageCount, validSmses.getSms().size());
	}

	/**
	 * Tries to read a non existent XML file.
	 *
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testReadNotExistent() throws Exception {
		// this file does not exist
		try {
			smsIO.readFrom(RES_NOT_EXISTENT);
		} catch (MessageIOException e) {
			// this should have been caused by a file not found exception
			if (!(e.toString().contains("FileNotFound")))
				throw e;
		}

		// null file
		File f = null;
		smsIO.readFrom(f);
	}

	/**
	 * Tries to read XML files with errors.
	 *
	 * @throws Exception
	 */
	@Test
	public void testReadBroken() throws Exception {
		// test reading of broken files
		Collection<Sms> results = new ArrayList<Sms>();

		for (int i = 0; i < RES_SMS_INVALIDS.length; i++) {
			InputStream is = TestUtils.getInputStreamForResource(RES_SMS_INVALIDS[i]);
			try {
				results = smsIO.readFrom(is);
			} catch (MessageIOException e) {
				// yep..
				//System.out.println(e.toString());
			}
		}
	}

	/**
	 * Writes a simple output file.
	 *
	 * @throws Exception
	 */
	@Test
	public void testWriteValidSimple() throws Exception {
		//String validPath = "fixtures/sms-20121011143146.xml";
		InputStream is = TestUtils.getInputStreamForResource(RES_SMS_NON_DUPES);

		Smses validSmses = new Smses(smsIO.readFrom(is));
		smsIO.writeTo(validSmses.getSms(), testOutputPath);

		is = TestUtils.getInputStreamForResource(RES_SMS_NON_DUPES);
		int lines1 = testUtils.readLineCount(is);
		int lines2 = testUtils.readLineCount(testOutputPath);
		assertTrue("Line count differs, expected " + lines1 + " but was " + lines2, lines1 == lines2);
	}

	/**
	 * Writes a simple output file with MMS.
	 *
	 * @throws Exception
	 */
	@Test
	public void testWriteValidSimpleWithMms() throws Exception {
		//String validPath = "fixtures/sms-20121011143146.xml";
		InputStream is = TestUtils.getInputStreamForResource(RES_SMS_AND_MMS);

		Smses validSmses = new Smses(smsIO.readFrom(is));
		smsIO.writeTo(validSmses.getSms(), testOutputPath);

		is = TestUtils.getInputStreamForResource(RES_SMS_AND_MMS);
		int lines1 = testUtils.readLineCount(is);
		int lines2 = testUtils.readLineCount(testOutputPath);

		// We're only exporting SMS, so line count will differ
		assertTrue(lines2 > 0);
		assertTrue(lines1 != lines2);
	}

	/**
	 * Tests if an exported XML contains the same messages as the source XML.
	 *
	 * @throws Exception
	 */
	@Ignore("Fixture missing")
	@Test
	public void testWriteValidExtended() throws Exception {
		// test if an exported XML contains the same messages as the source XML
		String validPath = "fixtures/sms-20121011143146.xml";

		// read original source file
		Collection<Sms> validSmses = smsIO.readFrom(validPath);

		// write back to temp file
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
}
