package de.leonhardt.sbm.gui.common.worker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.core.service.MessageIOService;
import de.leonhardt.sbm.gui.common.GuiUtils;
import lombok.extern.log4j.Log4j2;

/**
 * Background Worker to load the selected XML files.
 * @author Frederik Leonhardt
 *
 */
@Log4j2
public class ReadFilesWorker<T> extends SwingWorker<Collection<T>, Void> {

	private File[] files;
	private MessageIOService<T> mio;

	/**
	 * Creates a new read worker.
	 * @param mio - the message service to use
	 * @param files - the files to read
	 */
	public ReadFilesWorker(MessageIOService<T> mio, File[] files) {
		super();
		this.mio = mio;
		this.files = files;
	}

	@Override
    public Collection<T> doInBackground() {
		setText("Reading XML files..");

		Collection<T> smsList = new ArrayList<>();

		// try to read each file
		for (int i = 0; i<files.length; i++) {
			File curFile = files[i];
			try{
				Collection<T> smses = mio.readFrom(curFile);
				smsList.addAll(smses);

				// update progress
				Float progress = ((i+1.f)/files.length)*100.f;
				setProgress(progress.intValue());
				setText("Reading XML file(s).. %d%% - '%s'", progress.intValue(), curFile.getPath());

				// log status
				log.info("[Load] Read " + smses.size() +  " messages.");

			// raise any exceptions later as alert dialog in the EDT thread
			} catch (MessageIOException e) {
				log.error("MessageIO Error loading file: {}", e.toString(), e);
				raiseLater("Error loading file", "Could not load file '" + curFile.toString() + "'." + GuiUtils.BR + "Did you select a valid XML file?");
			} catch (IllegalArgumentException e) {
				log.error("Error loading file: {}", e.toString(), e);
				raiseLater("Error loading file", "Could not load file '" + curFile.toString() +"'.");
			}
		}
        return smsList;
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


    @Override
    protected void done() {
		log.debug("[Load] Done.");
		setText("Done!");
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
