package de.leonhardt.sbm.gui.handler;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import de.leonhardt.sbm.gui.GuiUtils;

/**
 * This LogHandler writes to a given JTextPane.
 * It also takes care about coloring the log entries.
 * 
 * @author Frederik Leonhardt
 * 
 */
public class CustomLogHandler extends Handler {

	private JTextPane textField;

	/**
	 * Creates a CustomHandler with a given JTextArea
	 * 
	 * @param textPane
	 */
	public CustomLogHandler(JTextPane textPane) {
		this.textField = textPane;
	}

	@Override
	public void close() throws SecurityException {
	}

	@Override
	public void flush() {
	}

	@Override
	public void publish(LogRecord record) {
		// check if message is loggable
		if (!isLoggable(record)) {
			return;
		}

		// format message
		SimpleFormatter sf = new SimpleFormatter();
		SimpleDateFormat sdf = new SimpleDateFormat("[yyy-MM-dd HH:mm:ss] ");
		Date now = new Date();

		// determine color for element
		Color color = Color.BLACK;

		int severity = record.getLevel().intValue();
		if (severity >= Level.INFO.intValue()) {
			color = Color.GREEN;
		}
		if (severity >= Level.WARNING.intValue()) {
			color = Color.ORANGE;
		}
		if (severity >= Level.SEVERE.intValue()) {
			color = Color.RED;
		}

		// print to GUI element
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setForeground(attrs, color);

		appendText(GuiUtils.BR + sdf.format(now) + "[" + record.getLevel() + "] "
						+ sf.formatMessage(record), attrs);

		// and scroll to the end..
		GuiUtils.autoscroll(textField);
	}

	/**
	 * Inserts some text to underlying JTextPane.
	 * 
	 * @param text
	 * @param set
	 * @param pos
	 */
	protected void insertText(String text, AttributeSet set, int pos) {
		try {
			textField.getDocument().insertString(pos, text, set);
		} catch (BadLocationException e) {
		}
	}

	/**
	 * Appends some text to underlying JTextPane.
	 * 
	 * @param text
	 * @param set
	 */
	protected void appendText(String text, AttributeSet set) {
		insertText(text, set, textField.getDocument().getLength());
	}

}
