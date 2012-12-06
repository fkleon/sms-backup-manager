package de.leonhardt.sbm.gui;

import javax.swing.JTextPane;

public class GuiUtils {
	
	// line separator
	static public String BR = System.getProperty("line.separator");

	public static void autoscroll(JTextPane jtp) {
		if (jtp.getSelectedText() == null) {
			jtp.setCaretPosition(jtp.getDocument().getLength());
		}
	}
	
}
