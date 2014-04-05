package de.leonhardt.sbm.smsbr.xml.debug;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class CustomValidationEventHandler implements ValidationEventHandler {

	@Override
	public boolean handleEvent(ValidationEvent event) {
        System.out.println("Event");
        System.out.println("Severity:  " + event.getSeverity());
        System.out.println("Message:  " + event.getMessage());
        System.out.println("Linked Exception:  " + event.getLinkedException());
        System.out.println("Locator:::");
        System.out.println("    Line Nbr:  " + event.getLocator().getLineNumber());
        System.out.println("    Column Nbr:  " + event.getLocator().getColumnNumber());
        System.out.println("    Offset:  " + event.getLocator().getOffset());
        System.out.println("    Objct:  " + event.getLocator().getObject());
        System.out.println("    Node:  " + event.getLocator().getNode());
        System.out.println("    URL:  " + event.getLocator().getURL());
        return true;
	}

}
