package de.leonhardt.sbm.gui.common.worker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.core.service.MessageConverterService;
import de.leonhardt.sbm.core.service.MessageService;
import de.leonhardt.sbm.gui.newGui.MessageView;
import de.leonhardt.sbm.gui.newGui.ViewController;

/**
 * Background Worker to import previously read messages into the MessageStore and populate the list afterwards.
 * This worker is supposed to start after the ImportXML worker, therefore it implements PropertyChangeListener.
 * @author Frederik Leonhardt
 *
 */
public class ImportMessagesWorker<T> extends SwingWorker<Void, MessageView> implements PropertyChangeListener {

	private Logger log;
	private MessageService bm;
	private MessageConverterService<T> msgConverter;
	private Collection<T> messagesToImport;
	private ViewController vc;
	
	/**
	 * Creates an import messages worker.
	 * @param bm - the messaging service to use
	 * @param msgConv - the message converter to use
	 * @param vc - the view controller to use
	 */
	public ImportMessagesWorker(MessageService bm, MessageConverterService<T> msgConv, ViewController vc) {
		this.bm = bm;
		this.msgConverter = msgConv;
		this.messagesToImport = new ArrayList<T>();
		this.vc = vc;
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
			
		setText("Done! Imported %d messages (incl. duplicates)", messagesToImport.size());
		
		// populate list
		vc.setDirty(); //TODO should we just add to the listmodel instead?
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
