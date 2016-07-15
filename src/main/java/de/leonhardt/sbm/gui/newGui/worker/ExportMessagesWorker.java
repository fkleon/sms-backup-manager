package de.leonhardt.sbm.gui.newGui.worker;

import java.io.File;
import java.util.Collection;

import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.core.model.Contact;
import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.gui.common.GuiUtils;
import de.leonhardt.sbm.gui.newGui.BackupManagerService;
import lombok.extern.log4j.Log4j2;

/**
 * Background Worker to export previously read messages to a given file.
 *
 * 1. Converts the messages to external format.
 * 2. Writes them to a file.
 *
 * @author Frederik Leonhardt
 *
 */
@Log4j2
public class ExportMessagesWorker<T> extends SwingWorker<Void, Void> {

	private BackupManagerService<T> bms;
	private boolean keepOriginals;
	private boolean exportDupes;
	private Contact selectedContact;
	private File destinationFile;
	private int msgCount;
	private boolean success;

	/**
	 * Creates an export message worker which exports all messages.
	 *
	 * @param f - the file to write to
	 * @param bms - the BMS to use
	 * @param keepOriginals - if originals should be kept
	 * @param exportDupes - if duplicates should be exported
	 */
	public ExportMessagesWorker(File f, BackupManagerService<T> bms, boolean keepOriginals, boolean exportDupes) {
		this(f, bms, keepOriginals, exportDupes, null);
	}

	/**
	 * Creates an export message worker.
	 *
	 * @param f - the file to write to
	 * @param bms - the BMS to use
	 * @param keepOriginals - if originals should be kept
	 * @param exportDupes - if duplicates should be exported
	 * @param selectedContact - the contact selected (only export selected) or null (export all)
	 */
	public ExportMessagesWorker(File f, BackupManagerService<T> bms, boolean keepOriginals, boolean exportDupes, Contact selectedContact) {
		this.destinationFile = f;
		this.bms = bms;
		this.keepOriginals = keepOriginals;
		this.exportDupes = exportDupes;
		this.selectedContact = selectedContact;
	}

	@Override
	protected Void doInBackground() throws Exception {
		setText("Exporting messages..");

		Collection<Message> toExport;
		if (selectedContact == null) {
			// export all messages
			toExport = bms.getMessageService().getMessages();

		} else {
			toExport = bms.getMessageService().getMessages(selectedContact);
		}

		msgCount = toExport.size();

		try {
			Collection<T> allSms = bms.getConverterService().toExternalCol(toExport, keepOriginals, exportDupes);
			bms.getMessageIO().writeTo(allSms, destinationFile);
		} catch (MessageIOException e) {
			log.warn("Failed to export messages: {}", e.toString(), e);
			raiseLater("Could not export messages", "There was a problem while exporting the messages: " + e.toString());
		}

		success = true;
		return null;
	}

	@Override
	public void done() {
		log.debug("[Export] Done.");

		if (success) {
			setText("Done! Exported %d messages to '%s'.", msgCount, destinationFile.getPath());
		} else {
			setText("Error! Could not export messages to '%s'.", destinationFile.getPath());
		}
	}

	/**
	 * Raises an error later.
	 * @param title
	 * @param msg
	 */
	private void raiseLater(final String title, final String msg) {
		SwingUtilities.invokeLater(new Runnable(){
		    @Override
		    public void run(){
		        GuiUtils.alertError(new JDesktopPane().getSelectedFrame(), title, msg);
		    }
		});
	}

    /**
     * Fires a property change (text)
     * @param text
     */
    private void setText(String text) {
		firePropertyChange("text", null, text);
    }

    /**
     * Fires a property change (text)
     * Takes a format string.
     * @param format
     * @param args
     */
    private void setText(String format, Object... args) {
    	firePropertyChange("text", null, String.format(format, args));
    }

}
