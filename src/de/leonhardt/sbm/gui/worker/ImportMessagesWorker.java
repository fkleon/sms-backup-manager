package de.leonhardt.sbm.gui.worker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import de.leonhardt.sbm.BackupManager;
import de.leonhardt.sbm.gui.renderer.CustomListModel;
import de.leonhardt.sbm.xml.model.Smses;

/**
 * Background Worker to import previously read messages into the MessageStore and populate the list afterwards.
 * This worker is supposed to start after the ImportXML worker, therefore it implements PropertyChangeListener.
 * @author Frederik Leonhardt
 *
 */
public class ImportMessagesWorker extends SwingWorker<Void, Void> implements PropertyChangeListener {

	private Logger log;
	private BackupManager bm;
	private List<Smses> messagesToImport;
	private CustomListModel clm;
	private int totalCount;
	private int duplicateCount;
	
	public ImportMessagesWorker(BackupManager bm, CustomListModel clm) {
		this.bm = bm;
		this.messagesToImport = new ArrayList<Smses>();
		this.clm = clm;
		this.log = Logger.getLogger("ImportMessagesWorker");
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		this.totalCount = 0;
		
		setText("Importing messages..");
		for (int i = 0; i<messagesToImport.size(); i++) {
			Smses smses = messagesToImport.get(i);
			bm.importMessages(smses);
			this.totalCount += smses.getSms().size();
			this.duplicateCount = bm.getMessages().countDuplicates() - duplicateCount;
			
			// update progress
			Float progress = ((i+1.f)/messagesToImport.size()*100.f);
			setProgress(progress.intValue());
			setText("Importing messages.. %d%%", progress.intValue());

			// log status
			log.info("[Import] Imported " + smses.getSms().size() + " messages.");
		}
		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		 if ("state".equals(event.getPropertyName())
                 && SwingWorker.StateValue.DONE == event.getNewValue()) {
			 SwingWorker<List<Smses>, Smses> worker = (SwingWorker<List<Smses>, Smses>) event.getSource();
			 try {
				this.messagesToImport = worker.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			 this.execute();
		 }
	}
	
	@Override
	public void done() {
		log.fine("[Import] Done.");
		
		// populate list
		clm.addElements(bm.getContacts());
		
		setText("Done! Imported %d messages (+%d duplicates)", totalCount, duplicateCount);
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
