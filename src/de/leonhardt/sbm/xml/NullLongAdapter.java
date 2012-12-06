package de.leonhardt.sbm.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class NullLongAdapter extends XmlAdapter<String, Long> {

	@Override
	public String marshal(Long arg0) throws Exception {
		if (arg0 == null) {
			//System.out.println(arg0);
			return "null";
		} else {
			return arg0.toString();
		}
	}

	@Override
	public Long unmarshal(String arg0) throws Exception {
		if (arg0 == null) {
			return null;
		} else {
			return Long.parseLong(arg0);
		}
	}

}
