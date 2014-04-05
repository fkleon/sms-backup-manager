package de.leonhardt.sbm.core.exception;

/**
 * Exception for an error while reading or writing a file.
 * 
 * @author Frederik Leonhardt
 *
 */
public class MessageIOException extends Exception {

	private static final long serialVersionUID = 247400595758190848L;

	public MessageIOException() {
		super();
	}

	public MessageIOException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MessageIOException(String arg0) {
		super(arg0);
	}

	public MessageIOException(Throwable arg0) {
		super(arg0);
	}

}
