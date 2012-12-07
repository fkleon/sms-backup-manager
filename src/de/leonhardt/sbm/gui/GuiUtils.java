package de.leonhardt.sbm.gui;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;

public class GuiUtils {
	
	// line separator
	static public String BR = System.getProperty("line.separator");

	/**
	 * Scrolls down the given JTextPane, if no text is selected.
	 * 
	 * @param jtp
	 */
	public static void autoscroll(JTextPane jtp) {
		if (jtp.getSelectedText() == null) {
			jtp.setCaretPosition(jtp.getDocument().getLength());
		}
	}
	
	
	public static void alertError(Component parent, String title, Object msg) {
		String msgTitle = title == null ? "" : title;
		JOptionPane.showMessageDialog(parent, msg, msgTitle, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void alertInfo(Component parent, String title, Object msg) {
		String msgTitle = title == null ? "" : title;
		JOptionPane.showMessageDialog(parent, msg, msgTitle, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static int alertSelection(Component parent, String title, Object msg) {
		String msgTitle = title == null ? "" : title;
		return JOptionPane.showConfirmDialog(parent, msg, msgTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	}
}
