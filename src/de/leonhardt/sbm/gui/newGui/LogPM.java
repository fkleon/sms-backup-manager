package de.leonhardt.sbm.gui.newGui;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

public class LogPM extends AbstractPM {

	TextPM logText = new TextPM();
	
	public LogPM() {
		PMManager.setup(this);
	}
	
	public void append(String text) {
		//TODO
	}
	
	public void appendLine(String text) {
		//TODO
	}
}
