package de.leonhardt.sbm.xml.debug;

import java.util.logging.Logger;

import javax.xml.bind.Unmarshaller.Listener;

public class CustomUnmarshallListener extends Listener {

	private Logger log = Logger.getLogger("UnmarshallListener");
	
	@Override
	public void beforeUnmarshal(Object target, Object parent) {
		log.fine("Before Unmarshal: "
				+ "\n Parent: " + (parent==null?"null":parent.toString())
				+ "\n Target: " + (target==null?"null":target.toString()));
		super.beforeUnmarshal(target, parent);
	}

	@Override
	public void afterUnmarshal(Object target, Object parent) {
		log.fine("After Unmarshal: "
				+ "\n Parent: " + (parent==null?"null":parent.toString())
				+ "\n Target: " + (target==null?"null":target.toString()));
		super.afterUnmarshal(target, parent);
	}

}
