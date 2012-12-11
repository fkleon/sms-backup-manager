package de.leonhardt.sbm;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.leonhardt.sbm.convert.SmsesConvert;
import de.leonhardt.sbm.util.MapUtil;
import de.leonhardt.sbm.xml.model.Sms;
import de.leonhardt.sbm.xml.model.Smses;

public class ManagerTest {

	private static SmsesIO smsIO;
	private static TestUtils testUtils;
	private static String testOutputPath;
	private static BackupManager bm;
	private static SmsesConvert msgConv;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		smsIO = new SmsesIO(true);
		testUtils = new TestUtils();
		bm = new BackupManager();
		msgConv = SmsesConvert.getInstance();
		testOutputPath = "test.xml";
	}
	

	@Before
	public void beforeTests() {
		bm.clear();
	}
	
	@Test
	public void simpleDupeTest() throws Exception {
		assertEquals("BM not empty.",0,bm.getMessages().size());
		
		String validPath = "fixtures/sms-dupes.xml"; // says 2444 but has 8

		int messageCount = 8;
		int dupeCount = 1;
		int contactCount = 4;
		
		Smses validSmses = smsIO.readFrom(validPath);
		
		assertEquals("Message number mismatch #import",messageCount,validSmses.getSms().size());
	
		bm.importMessages(validSmses);
		
		assertEquals("Contact number mismatch",contactCount,bm.getContacts().size());
		assertEquals("Duplicate number mismatch",dupeCount,bm.getMessages().countDuplicates());		
		assertEquals("Message number mismatch",messageCount,bm.getMessages().size()+bm.getMessages().countDuplicates());
		assertTrue(bm.getMessages().size()<validSmses.getCount());
		assertEquals("Message number mismatch #2",validSmses.getSms().size(),bm.getMessages().size()+dupeCount);
	}
	
	@Test
	public void simpleNonDupeTest() throws Exception {
		assertEquals("BM not empty.",0,bm.getMessages().size());
		
		String validPath = "fixtures/sms-non-dupes.xml"; // messages with same content and contact, but different date/type

		int messageCount = 2;
		int dupeCount = 0;
		int contactCount = 1;
		
		Smses validSmses = smsIO.readFrom(validPath);
		
		bm.importMessages(validSmses);

		assertEquals("Contact number mismatch",contactCount,bm.getContacts().size());
		assertEquals("Duplicate number mismatch",dupeCount,bm.getMessages().countDuplicates());		
		assertEquals("Message number mismatch",messageCount,bm.getMessages().size()+bm.getMessages().countDuplicates());
	}
	
	@Test
	public void simpleNonDupeTest2() throws Exception {
		assertEquals("BM not empty.",0,bm.getMessages().size());
		
		String validPath = "fixtures/sms-non-dupes-2.xml"; // messages with same content, date and type, but different contact

		int messageCount = 2;
		int dupeCount = 0;
		int contactCount = 2;
		
		Smses validSmses = smsIO.readFrom(validPath);
		
		bm.importMessages(validSmses);

		MapUtil.dumpMap(bm.getCS());
		
		assertEquals("Contact number mismatch",contactCount,bm.getContacts().size());
		assertEquals("Duplicate number mismatch",dupeCount,bm.getMessages().countDuplicates());		
		assertEquals("Message number mismatch",messageCount,bm.getMessages().size()+bm.getMessages().countDuplicates());
	}
	
	@Test
	public void converterTest() throws Exception {
		int messageCount = 8;
		int dupeCount = 1;
		
		Smses validSmses = smsIO.readFrom("fixtures/sms-dupes.xml");
		
		bm.importMessages(validSmses);
		
		assertEquals("Message number mismatch",messageCount,bm.getMessages().size()+bm.getMessages().countDuplicates());

		Smses newSmses = msgConv.convert(bm.getMessages(),true,true);
		
		assertEquals("Message number mismatch",messageCount,newSmses.getCount().intValue());
		assertEquals("Message number mismatch",messageCount,newSmses.getSms().size());

		newSmses = msgConv.convert(bm.getMessages(),true,false);

		assertEquals("Message number mismatch",messageCount-dupeCount,newSmses.getCount().intValue());
		assertEquals("Message number mismatch",messageCount-dupeCount,newSmses.getSms().size());
	}
	
	@Test
	public void extendedDupeTest() throws Exception {
		String validPath = "fixtures/sms-20121011143146.xml";
		
		int messageCount = 2444;
		int dupeCount = 0;
		
		Smses validSmses = smsIO.readFrom(validPath);
		
		assertEquals("Message number mismatch #import", messageCount, validSmses.getSms().size());
		
		bm.importMessages(validSmses);
		
		Smses newSmses = msgConv.convert(bm.getMessages(),true,true);
		
		for (Sms sms: validSmses.getSms()) {
			if (!newSmses.getSms().contains(sms)) {
				System.out.println("Expected sms: " + sms);
			}
		}
		
		assertEquals("Duplicate number mismatch",dupeCount,bm.getMessages().countDuplicates());		
		assertEquals("Message number mismatch",messageCount,bm.getMessages().size()+bm.getMessages().countDuplicates());
		assertTrue(bm.getMessages().size()<=validSmses.getCount());
		assertEquals("Message number mismatch #2",validSmses.getSms().size(),bm.getMessages().size()+dupeCount);
	}
	
}
