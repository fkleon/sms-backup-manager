package de.leonhardt.sbm.exception;

public class UnknownStatusException extends Exception {

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

	private static final long serialVersionUID = 247400595758190848L;

}
