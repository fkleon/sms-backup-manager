package de.leonhardt.sbm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import de.leonhardt.sbm.core.model.Contact;
import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.core.model.MessageConsts.Protocol;
import de.leonhardt.sbm.core.model.MessageConsts.Status;
import de.leonhardt.sbm.core.model.MessageConsts.Type;

/**
 * Class which generates random messages and contacts for testing purpose.
 * 
 * @author Frederik Leonhardt
 *
 */
public class MessageGenerator {

	// some basic configuration
	public static int maxMsgLength = 500;
	public static int minContacts = 2;
	public static int maxContacts = 50;
	public static int maxMsgsPerContact = 50;
	
	// the ranom generator to use
	private Random rand;
	
	/**
	 * Creates new MessageGenerator
	 */
	public MessageGenerator() {
		this.rand = new Random();
	}
	
	/**
	 * Creates new MessageGenerator with given seed
	 * @param seed
	 */
	public MessageGenerator(long seed) {
		this.rand = new Random(seed);
	}
	
	/**
	 * Generates between minContacts and maxContacts contacts
	 * and a maximum of maxMsgPerContact messages per contact.
	 * 
	 * @return
	 */
	public Collection<Message> generateMessages() {
		// generate contacts
		Collection<Contact> contacts = generateContacts();
		Collection<Message> messages = generateMessagesFor(contacts);
		
		System.out.println(String.format("Generated %d messages for %d contacts.", messages.size(), contacts.size()));
		
		return messages;
	}
	
	/**
	 * Generates between minContacts and maxContacts contacts.
	 * 
	 * @return
	 */
	public Collection<Contact> generateContacts() {
		int num = getRandomInt(minContacts, maxContacts);

		Collection<Contact> contacts = new ArrayList<Contact>();
		// generate N random contacts
		for (int i = 0; i < num; i++) {
			contacts.add(new Contact(getRandomContactName(), getRandomPhoneNumber()));
		}
		return contacts;
	}
	
	/**
	 * Generates the given number of contacts.
	 * 
	 * @param num
	 * @return
	 */
	public Collection<Contact> generateContacts(int num) {
		Collection<Contact> contacts = new ArrayList<Contact>();
		// generate N random contacts
		for (int i = 0; i < num; i++) {
			contacts.add(new Contact(getRandomContactName(), getRandomPhoneNumber()));
		}
		return contacts;
	}
	
	/**
	 * Generates messages for the given contacts.
	 * Each contact will have 1-maxMsgPerContact messages.
	 * 
	 * @param contacts
	 * @return
	 */
	public Collection<Message> generateMessagesFor(Collection<Contact> contacts) {
		Collection<Message> messages = new ArrayList<Message>();

		for (Contact c: contacts) {
			// determine message count
			int msgCount = getRandomInt(1, maxMsgsPerContact);
			for (int j = 0; j < msgCount; j++) {
				Date now = new Date(System.currentTimeMillis());
				String msgBody = RandomStringUtils.randomAscii(getRandomInt(1, maxMsgLength));
				Type msgType = getRandomBool() ? Type.Received : Type.Sent;
				Integer msgRead = getRandomInt(0, 1);
				Status msgStatus = (getRandomInt(0,10)<10) ? Status.None : Status.Failed;
				
				Message msg = new Message(j+1);
				msg.setBody(msgBody);
				msg.setContact(c);
				msg.setDate(now);
				msg.setDateSent(msgType == Type.Sent ? now : null);
				msg.setOriginalAddress("n/a");
				msg.setOriginalContactName("n/a");
				msg.setProtocol(Protocol.SMS);
				msg.setRead(msgRead);
				msg.setServiceCenter("+491111111");
				msg.setStatus(msgStatus);
				msg.setSubject(null);
				msg.setType(msgType);
				
				messages.add(msg);
			}
		}
		
		return messages;
	}
	
	/**
	 * Returens a random integer in the interval [min,max]
	 * @param min
	 * @param max
	 * @return
	 */
	private int getRandomInt(int min, int max) {
		return rand.nextInt(max - min + 1) + min;
	}
	
	/**
	 * Generates a random telephone number prefix
	 * @return
	 */
	private int getRandomPrefix() {
		return getRandomInt(1, 99);
	}
	
	/**
	 * Generates a random telephone number.
	 * @return
	 */
	private String getRandomPhoneNumber() {
		return String.format("+%d %d", getRandomPrefix(), getRandomInt(10000, 9999999));
	}
	
	/**
	 * Generates a random contact name, consisting of first and last name.
	 * With 50% Chance, the contact will have a middle name as well.
	 * 
	 * @return
	 */
	private String getRandomContactName() {
		String firstName = RandomStringUtils.randomAlphabetic(getRandomInt(3, 10));
		String lastName = RandomStringUtils.randomAlphabetic(getRandomInt(3, 10));
		
		if (getRandomBool()) {
			// insert middlename
			String middleName = RandomStringUtils.randomAlphabetic(getRandomInt(3, 10));
			return String.format("%s %s %s", firstName, middleName, lastName);
		} else {
			return String.format("%s %s", firstName, lastName);
		}
	}
	
	/**
	 * Generates a random boolean.
	 * @return
	 */
	private boolean getRandomBool() {
		if (getRandomInt(0, 1) < 1) {
			return true;
		} else {
			return false;
		}
	}
	
}
