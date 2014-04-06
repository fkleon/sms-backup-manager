package de.leonhardt.sbm.gui.common;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

/**
 * A bunch of utilities related to GUI actions.
 * 
 * @author Frederik Leonhardt
 */
public class GuiUtils {
	
	// system line separator
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
	
	/**
	 * Returns a new JTextArea in label style.
	 * 
	 * @param text
	 * @return
	 */
	public static JTextArea buildLabelStyleTextArea(String text) {
		JTextArea jTA = new JTextArea(text);
		jTA.setEditable(false);
		jTA.setCursor(null);
		jTA.setOpaque(false);
		jTA.setFocusable(false);
		return jTA;
	}
	
	/**
	 * Applies label style to a given TextArea.
	 * 
	 * @param jTA
	 * @return
	 */
	public static <T extends JTextArea> T makeLabelStyleTextArea(T jTA) {
		jTA.setEditable(false);
		jTA.setCursor(null);
		jTA.setOpaque(false);
		jTA.setFocusable(false);
		return jTA;
	}
	  
	/** 
	 * Sets cursor for specified frame to "Wait" cursor
	 * 
	 *  @param frame
	 */
	public static void startWaitCursor(JFrame frame) {
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		frame.getGlassPane().setVisible(true);
	}
	  
	/**
	 * Sets cursor for specified frame to "Default" cursor
	 * 
	 * @param frame
	 */
	public static void stopWaitCursor(JFrame frame) {
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		frame.getGlassPane().setVisible(false);
	}
	
	/**
	 * Opens an alert dialog.
	 * 
	 * @param parent
	 * @param title
	 * @param msg
	 */
	public static void alertError(Component parent, String title, Object msg) {
		String msgTitle = title == null ? "" : title;
		JOptionPane.showMessageDialog(parent, msg, msgTitle, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Opens an info dialog.
	 * 
	 * @param parent
	 * @param title
	 * @param msg
	 */
	public static void alertInfo(Component parent, String title, Object msg) {
		String msgTitle = title == null ? "" : title;
		JOptionPane.showMessageDialog(parent, msg, msgTitle, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Opens a selection dialog.
	 * 
	 * @param parent
	 * @param title
	 * @param msg
	 * @return return value
	 */
	public static int alertSelection(Component parent, String title, Object msg) {
		String msgTitle = title == null ? "" : title;
		return JOptionPane.showConfirmDialog(parent, msg, msgTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	}
}
