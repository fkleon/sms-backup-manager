package de.leonhardt.sbm.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.leonhardt.sbm.TestUtils;
import de.leonhardt.sbm.core.BackupManager;
import de.leonhardt.sbm.core.service.MessageConverterService;
import de.leonhardt.sbm.core.service.MessageIOService;
import de.leonhardt.sbm.core.util.MapUtil;
import de.leonhardt.sbm.smsbr.SmsBrConverter;
import de.leonhardt.sbm.smsbr.SmsBrIO;
import de.leonhardt.sbm.smsbr.xml.model.Sms;
import de.leonhardt.sbm.smsbr.xml.model.Smses;

/**
 * Tests the {@link MessageConverterService} implementation {@link SmsBrConverter},
 * and the {@link BackupManager}.
 * 
 * @author Frederik Leonhardt
 *
 */
public class ManagerTest {

	private MessageIOService<Sms> smsIO;
	private static TestUtils testUtils;
	private static String testOutputPath;
	private BackupManager bm;
	private static MessageConverterService<Sms> msgConv;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testUtils = new TestUtils();
		msgConv = new SmsBrConverter();
		testOutputPath = "test.xml";
	}
	
	@After
	public void cleanUpAfterTests() {
		bm.clear();
	}
	
	/**
	 * 
	 * SmsBr and BackupManager are not thread safe, create new objects
	 * for each test to allow multi-threaded test executions.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUpBeforeTests() throws Exception {
		smsIO = new SmsBrIO(false);
		bm = new BackupManager();
	}
	
	@Test
	public void simpleDupeTest() throws Exception {
		assertEquals("BM not empty.", 0, bm.getMessages().size());
		
		String validPath = "fixtures/sms-dupes.xml"; // says 2444 but has 8
		InputStream validIs = TestUtils.getInputStreamFromResource(validPath);
		
		int messageCount = 8;
		int dupeCount = 1;
		int contactCount = 4;
		
		Smses validSmses = new Smses(smsIO.readFrom(validIs));
		
		assertEquals("Message number mismatch #import", messageCount, validSmses.getSms().size());
	
		bm.importMessages(msgConv.toInternalCol(validSmses.getSms()));
		
		assertEquals("Contact number mismatch", contactCount, bm.getContacts().size());
		assertEquals("Duplicate number mismatch", dupeCount, bm.getMessages().countDuplicates());		
		assertEquals("Message number mismatch", messageCount, bm.getMessages().size() + bm.getMessages().countDuplicates());
		assertTrue(bm.getMessages().size() < validSmses.getCount());
		assertEquals("Message number mismatch #2", validSmses.getSms().size(), bm.getMessages().size() + dupeCount);
	}
	
	@Test
	public void simpleNonDupeTest() throws Exception {
		assertEquals("BM not empty.", 0, bm.getMessages().size());
		
		String validPath = "fixtures/sms-non-dupes.xml"; // messages with same content and contact, but different date/type
		InputStream validIs = TestUtils.getInputStreamFromResource(validPath);

		int messageCount = 2;
		int dupeCount = 0;
		int contactCount = 1;
		
		Smses validSmses = new Smses(smsIO.readFrom(validIs));
		
		bm.importMessages(msgConv.toInternalCol(validSmses.getSms()));

		assertEquals("Contact number mismatch", contactCount, bm.getContacts().size());
		assertEquals("Duplicate number mismatch", dupeCount, bm.getMessages().countDuplicates());		
		assertEquals("Message number mismatch", messageCount, bm.getMessages().size() + bm.getMessages().countDuplicates());
	}
	
	@Test
	public void simpleNonDupeTest2() throws Exception {
		assertEquals("BM not empty.",0,bm.getMessages().size());
		
		String validPath = "fixtures/sms-non-dupes-2.xml"; // messages with same content, date and type, but different contact
		InputStream validIs = TestUtils.getInputStreamFromResource(validPath);

		int messageCount = 2;
		int dupeCount = 0;
		int contactCount = 2;
		
		Smses validSmses = new Smses(smsIO.readFrom(validIs));
		
		bm.importMessages(msgConv.toInternalCol(validSmses.getSms()));

		//MapUtil.dumpMap(bm.getCS());
		
		assertEquals("Contact number mismatch", contactCount, bm.getContacts().size());
		assertEquals("Duplicate number mismatch", dupeCount, bm.getMessages().countDuplicates());		
		assertEquals("Message number mismatch", messageCount, bm.getMessages().size() + bm.getMessages().countDuplicates());
	}
	
	@Test
	public void converterTest() throws Exception {
		int messageCount = 8;
		int dupeCount = 1;
		
		String validPath = "fixtures/sms-dupes.xml";
		InputStream validIs = TestUtils.getInputStreamFromResource(validPath);
		
		Smses validSmses =  new Smses(smsIO.readFrom(validIs));
		
		bm.importMessages(msgConv.toInternalCol(validSmses.getSms()));
		
		assertEquals("Message number mismatch", messageCount, bm.getMessages().size() + bm.getMessages().countDuplicates());

		Collection<Sms> extSmses = msgConv.toExternalCol(bm.getMessages(), true, true);
		Smses newSmses = new Smses(new ArrayList<Sms>(extSmses));
		
		assertEquals("Message number mismatch", messageCount, newSmses.getCount().intValue());
		assertEquals("Message number mismatch", messageCount, newSmses.getSms().size());

		extSmses = msgConv.toExternalCol(bm.getMessages(), true, false);
		newSmses = new Smses(new ArrayList<Sms>(extSmses));
		
		assertEquals("Message number mismatch", messageCount - dupeCount, newSmses.getCount().intValue());
		assertEquals("Message number mismatch", messageCount - dupeCount, newSmses.getSms().size());
	}
	
	@Ignore("Missing fixture")
	@Test
	public void extendedDupeTest() throws Exception {
		String validPath = "fixtures/sms-20121011143146.xml";
		InputStream validIs = TestUtils.getInputStreamFromResource(validPath);

		int messageCount = 2444;
		int dupeCount = 0;
		
		Smses validSmses = new Smses(smsIO.readFrom(validIs));
		
		assertEquals("Message number mismatch #import", messageCount, validSmses.getSms().size());
		
		bm.importMessages(msgConv.toInternalCol(validSmses.getSms()));
		
		Collection<Sms> extSmses = msgConv.toExternalCol(bm.getMessages(), true, true);
		Smses newSmses = new Smses(new ArrayList<Sms>(extSmses));
		
		for (Sms sms: validSmses.getSms()) {
			if (!newSmses.getSms().contains(sms)) {
				System.out.println("Expected sms: " + sms);
			}
		}
		
		assertEquals("Duplicate number mismatch", dupeCount, bm.getMessages().countDuplicates());		
		assertEquals("Message number mismatch", messageCount, bm.getMessages().size() + bm.getMessages().countDuplicates());
		assertTrue(bm.getMessages().size()<=validSmses.getCount());
		assertEquals("Message number mismatch #2", validSmses.getSms().size(), bm.getMessages().size() + dupeCount);
	}
	
}
