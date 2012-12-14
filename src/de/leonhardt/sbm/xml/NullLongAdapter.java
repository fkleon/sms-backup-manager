package de.leonhardt.sbm.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * The NullLongAdapter is used to marshal and
 * unmarshal null-Longs ("null") in an XML document.
 *  
 * @author Frederik Leonhardt
 *
 */
public class NullLongAdapter extends XmlAdapter<String, Long> {

	/**
	 * Marshals a given Long.
	 * 
	 * @param the Long to marshal
	 * @return "null"-String, if given Long was null.
	 */
	@Override
	public String marshal(Long arg0) throws Exception {
		if (arg0 == null) {
			//System.out.println(arg0);
			return "null";
		} else {
			return arg0.toString();
		}
	}

	/**
	 * Unmarshals given String.
	 * 
	 * @param the String to unmarshal
	 * @return null, if given String was null.
	 */
	@Override
	public Long unmarshal(String arg0) throws Exception {
		if (arg0 == null) {
			return null;
		} else {
			return Long.parseLong(arg0);
		}
	}

}
