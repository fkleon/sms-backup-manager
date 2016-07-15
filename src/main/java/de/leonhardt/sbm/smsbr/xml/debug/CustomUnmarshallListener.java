package de.leonhardt.sbm.smsbr.xml.debug;

import javax.xml.bind.Unmarshaller.Listener;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomUnmarshallListener extends Listener {

	@Override
	public void beforeUnmarshal(Object target, Object parent) {
		log.debug("Before Unmarshal: "
				+ "\n Parent: " + (parent==null?"null":parent.toString())
				+ "\n Target: " + (target==null?"null":target.toString()));
		super.beforeUnmarshal(target, parent);
	}

	@Override
	public void afterUnmarshal(Object target, Object parent) {
		log.debug("After Unmarshal: "
				+ "\n Parent: " + (parent==null?"null":parent.toString())
				+ "\n Target: " + (target==null?"null":target.toString()));
		super.afterUnmarshal(target, parent);
	}

}
