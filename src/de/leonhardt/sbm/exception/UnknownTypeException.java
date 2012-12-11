package de.leonhardt.sbm.exception;

public class UnknownTypeException extends Exception {

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

	private static final long serialVersionUID = 247400595758190848L;

}
