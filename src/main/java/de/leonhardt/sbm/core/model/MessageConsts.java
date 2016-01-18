package de.leonhardt.sbm.core.model;

import de.leonhardt.sbm.core.exception.UnknownProtocolException;
import de.leonhardt.sbm.core.exception.UnknownStatusException;
import de.leonhardt.sbm.core.exception.UnknownTypeException;

/**
 * This class holds enumerations used in messages.
 * 
 * @author Frederik Leonhardt
 *
 */
public class MessageConsts {

	/**
	 * Protocol of a message.
	 * Each protocol is represented by an unique integer.
	 * 
	 * @author Frederik Leonhardt
	 */
	public enum Protocol {
		SMS(0),
		MMS(1),
		BROADCAST(57), // No sender, just name
		PUSH(65);
		
		private final int value;    
		
		private Protocol(int value) {
			this.value = value;
		}
		
		public static Protocol toProtocol(int num) throws UnknownProtocolException {
			if (num == 0) return SMS;
			if (num == 1) return MMS;
			if (num == 57) return BROADCAST;
			if (num == 65) return PUSH; //TODO not sure
			throw new UnknownProtocolException("Unknown protocol: " + num);
		}

		public int getValue() {
			return value;
		}
	}
	
	/**
	 * Type of a message.
	 * Each type is represented by an unique integer.
	 * Each type has an icon name.
	 * 
	 * @author Frederik Leonhardt
	 */
	public enum Type {
		Received(1, "message-in.png"),
		Sent(2, "message-out.png"),
		Draft(3, "message-draft.png"),
		Outbox(4 , "message-outbox.png"),
		Failed(5, "message-fail.png"),
		Queued(6, "message-queue.png");
	
		private final int value;    
		private final String icon;
		
		private Type(int value, String icon) {
			this.value = value;
			this.icon = icon;
		}
		
		public static Type toType(int num) throws UnknownTypeException {
			if (num == 1) return Received;
			if (num == 2) return Sent;
			if (num == 3) return Draft;
			if (num == 4) return Outbox;
			if (num == 5) return Failed;
			if (num == 6) return Queued;
			throw new UnknownTypeException("Unknown type: " + num);
		}
		
		public String getIcon() {
			return icon;
		}

		public int getValue() {
			return value;
		}
	}
	
	/**
	 * Status of a message.
	 * Each status is represented by an unique integer.
	 * Each status has an icon name.
	 * 
	 * @author Frederik Leonhardt
	 */
	public enum Status {
		None(-1, "status-none.png"),
		Complete(0, "status-ok.png"),
		Pending(32, "status-pending.png"),
		Failed(64, "status-fail.png");
		
		private final int value;
		private String icon;

		private Status(int value, String icon) {
			this.value = value;
			this.icon = icon;
		}
		
		public static Status toStatus(int num) throws UnknownStatusException {
			if (num == -1) return None;
			if (num == 0) return Complete;
			if (num == 32) return Pending;
			if (num == 64) return Failed;
			throw new UnknownStatusException("Unknown type: " + num);
		}
		
		public String getIcon() {
			return icon;
		}

		public int getValue() {
			return value;
		}
	}
}
