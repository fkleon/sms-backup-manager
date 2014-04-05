package de.leonhardt.sbm.core.exception;

/**
 * Exception for an unknown status while importing a message.
 * 
 * @author Frederik Leonhardt
 *
 */
public class UnknownStatusException extends Exception {

	private static final long serialVersionUID = 247400595758190848L;

	public UnknownStatusException() {
		super();
	}

	public UnknownStatusException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnknownStatusException(String arg0) {
		super(arg0);
	}

	public UnknownStatusException(Throwable arg0) {
		super(arg0);
	}
}
