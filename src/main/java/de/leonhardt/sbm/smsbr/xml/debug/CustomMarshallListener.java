package de.leonhardt.sbm.smsbr.xml.debug;

import javax.xml.bind.Marshaller.Listener;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomMarshallListener extends Listener {

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
