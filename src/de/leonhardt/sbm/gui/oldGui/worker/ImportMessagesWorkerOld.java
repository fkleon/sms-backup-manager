package de.leonhardt.sbm.gui.oldGui.worker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import de.leonhardt.sbm.core.BackupManager;
import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.core.service.MessageConverterService;
import de.leonhardt.sbm.gui.oldGui.renderer.CustomListModel;

/**
 * Background Worker to import previously read messages into the MessageStore and populate the list afterwards.
 * This worker is supposed to start after the ImportXML worker, therefore it implements PropertyChangeListener.
 * @author Frederik Leonhardt
 *
 */
public class ImportMessagesWorkerOld<T> extends SwingWorker<Void, Void> implements PropertyChangeListener {

	private Logger log;
	private BackupManager bm;
	private MessageConverterService<T> msgConverter;
	private Collection<T> messagesToImport;
	private CustomListModel clm;
	
	public ImportMessagesWorkerOld(BackupManager bm, MessageConverterService<T> msgConv, CustomListModel clm) {
		this.bm = bm;
		this.msgConverter = msgConv;
		this.messagesToImport = new ArrayList<T>();
		this.clm = clm;
		this.log = Logger.getLogger("ImportMessagesWorker");
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		setText("Importing messages..");
		int i = 0;
		for (T msg: messagesToImport) {
			// import messages
			Message message = msgConverter.toInternalMessage(msg);
			bm.importMessage(message);

			// update progress
			Float progress = ((i+1.f)/messagesToImport.size()*100.f);
			setProgress(progress.intValue());
			setText("Importing messages.. %d%%", progress.intValue());
			
			i++;
		}
		// log status
		log.info("[Import] Imported " + messagesToImport.size() + " messages.");
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		 if ("state".equals(event.getPropertyName())
                 && SwingWorker.StateValue.DONE == event.getNewValue()
                 && event.getSource() instanceof SwingWorker) {
			 SwingWorker<Collection<T>, T> worker = (SwingWorker<Collection<T>, T>) event.getSource();
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
		
		setText("Done! Imported %d messages (incl. duplicates)", messagesToImport.size());
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
