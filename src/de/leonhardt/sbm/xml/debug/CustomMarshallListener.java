package de.leonhardt.sbm.xml.debug;

import java.util.logging.Logger;

import javax.xml.bind.Marshaller.Listener;

public class CustomMarshallListener extends Listener {

	private Logger log = Logger.getLogger("MarshallListener");

	@Override
	public void afterMarshal(Object arg0) {
		log.info("After Marshal: " + (arg0==null?"null":arg0.toString()));
		super.afterMarshal(arg0);
	}

	@Override
	public void beforeMarshal(Object arg0) {
		log.info("Before Marshal: " + (arg0==null?"null":arg0.toString()));
		super.beforeMarshal(arg0);
	}

}
