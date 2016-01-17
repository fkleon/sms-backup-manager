package de.leonhardt.sbm.smsbr;

import java.util.Collection;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.leonhardt.sbm.MessageGenerator;
import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.core.exception.UnknownProtocolException;
import de.leonhardt.sbm.core.exception.UnknownStatusException;
import de.leonhardt.sbm.core.exception.UnknownTypeException;
import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.core.model.MessageConsts.Protocol;
import de.leonhardt.sbm.core.model.MessageConsts.Status;
import de.leonhardt.sbm.core.model.MessageConsts.Type;
import de.leonhardt.sbm.core.service.MessageConverterService;
import de.leonhardt.sbm.smsbr.xml.model.Sms;

/**
 * Test the {@link MessageConverterService} implementation
 * {@link SmsBrConverter}.
 * 
 * @author Frederik Leonhardt
 * 
 */
public class SmsBrConverterTest {

	private static MessageConverterService<Sms> msgConv;
	private SmsBrFixtureLoader smsBrFixtureLoader;
	private MessageGenerator messageGenerator;

	@BeforeClass
	public static void setUpBeforeClass() {
		msgConv = new SmsBrConverter();
	}

	@Before
	public void setUpBeforeTests() throws MessageIOException {
		smsBrFixtureLoader = new SmsBrFixtureLoader();
		messageGenerator = new MessageGenerator();
	}

	@Test
	public void toInternalMessage() throws Exception {
		//TODO more fixtures
		Collection<Sms> smses = smsBrFixtureLoader
				.loadSmses("/fixtures/sms-non-dupes.xml");

		for (Sms sms : smses) {
			Message msg = msgConv.toInternalMessage(sms);
			compareMessages(msg, sms, false);
			//Assert.assertNull("Id should be not set.", msg.getId());
			Assert.assertTrue("Id should not be valid.", msg.getId() < 0);
		}
	}

	@Test
	public void toInternalCol() throws Exception {
		//TODO more fixtures
		Collection<Sms> smses = smsBrFixtureLoader
				.loadSmses("/fixtures/sms-non-dupes.xml");

		Collection<Message> msges = msgConv.toInternalCol(smses);
		
		// With no dupes, should have same size
		Assert.assertNotNull(msges);
		Assert.assertEquals(smses.size(), msges.size());
		
		//TODO test contents (sorting is not equal!)
	}

	@Test
	public void toExternalMessage() throws Exception {
		boolean KEEP_ORIGINALS = true;

		//TODO don't use random fixtures
		Collection<Message> msges = messageGenerator.generateMessages();
		
		for (Message msg: msges) {
			Sms sms = msgConv.toExternalMessage(msg, KEEP_ORIGINALS);
			compareMessages(msg, sms, KEEP_ORIGINALS);
		}
	}

	@Test
	public void toExternalCol() {
		boolean KEEP_ORIGINALS = true;
		
		//TODO don't use random fixtures
		Collection<Message> msges = messageGenerator.generateMessages();
		Collection<Sms> smses = msgConv.toExternalCol(msges, KEEP_ORIGINALS, false);
		
		// With no dupes, should have same size
		Assert.assertNotNull(smses);
		Assert.assertEquals(msges.size(), smses.size());
		
		//TODO test contents (sorting is not equal!)
	}

	/**
	 * Compares the given messages.
	 * 
	 * @param msg
	 * @param sms
	 * @baram keptOriginals
	 * @throws UnknownProtocolException
	 * @throws UnknownStatusException
	 * @throws UnknownTypeException
	 */
	private void compareMessages(Message msg, Sms sms, boolean keptOriginals)
			throws UnknownProtocolException, UnknownStatusException,
			UnknownTypeException {
		Assert.assertEquals(msg.getBody(), sms.getBody());
		Assert.assertEquals(msg.getOriginalAddress(), sms.getAddress());
		Assert.assertEquals(msg.getOriginalContactName(), sms.getContactName());
		Assert.assertEquals(msg.getServiceCenter(), sms.getServiceCenter());
		Assert.assertEquals(msg.getSubject(), sms.getSubject());
		Assert.assertEquals(msg.getDate(), new Date(sms.getDate().longValue()));
		Assert.assertEquals(msg.getProtocol(), Protocol.toProtocol(sms.getProtocol()));
		Assert.assertEquals(msg.getRead(), Integer.valueOf(sms.getRead()));
		Assert.assertEquals(msg.getStatus(), Status.toStatus(sms.getStatus()));
		Assert.assertEquals(msg.getType(), Type.toType(sms.getType()));

		if (Type.Sent.equals(msg.getType()) && sms.getDateSent() != null) {
			// should have sent date
			Assert.assertNotNull("Should have a sent date.", msg.getDateSent());
			Assert.assertEquals(msg.getDateSent(), new Date(sms.getDateSent().longValue()));
		}
		
		Assert.assertNotNull(msg.getContact());
		
		if (!keptOriginals) {
			Assert.assertEquals(msg.getContact().getContactName(), sms.getContactName().trim());
		}
	}

}
