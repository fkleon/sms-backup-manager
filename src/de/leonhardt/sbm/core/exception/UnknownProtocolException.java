package de.leonhardt.sbm.core.exception;

/**
 * Exception for an unknown protocol while importing a message.
 * 
 * @author Frederik Leonhardt
 *
 */
public class UnknownProtocolException extends Exception {

	private static final long serialVersionUID = 247400595758190848L;

	public UnknownProtocolException() {
		super();
	}

	public UnknownProtocolException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnknownProtocolException(String arg0) {
		super(arg0);
	}

	public UnknownProtocolException(Throwable arg0) {
		super(arg0);
	}

}
