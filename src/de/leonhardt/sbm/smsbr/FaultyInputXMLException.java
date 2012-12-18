package de.leonhardt.sbm.smsbr;

/**
 * Exception for a faulty XML.
 * 
 * @author Frederik Leonhardt
 *
 */
public class FaultyInputXMLException extends Exception {

	public FaultyInputXMLException() {
		super();
	}

	public FaultyInputXMLException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FaultyInputXMLException(String arg0) {
		super(arg0);
	}

	public FaultyInputXMLException(Throwable arg0) {
		super(arg0);
	}

	private static final long serialVersionUID = 247400595758190848L;

}
