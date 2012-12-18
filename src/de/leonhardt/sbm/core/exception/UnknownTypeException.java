package de.leonhardt.sbm.core.exception;

/**
 * Exception for an unknown type while importing a message.
 * 
 * @author Frederik Leonhardt
 *
 */
public class UnknownTypeException extends Exception {

	private static final long serialVersionUID = 247400595758190848L;

	public UnknownTypeException() {
		super();
	}

	public UnknownTypeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnknownTypeException(String arg0) {
		super(arg0);
	}

	public UnknownTypeException(Throwable arg0) {
		super(arg0);
	}
}
