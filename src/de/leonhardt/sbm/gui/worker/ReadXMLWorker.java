package de.leonhardt.sbm.gui.worker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.xml.bind.JAXBException;

import de.leonhardt.sbm.MessageIO;
import de.leonhardt.sbm.exception.FaultyInputXMLException;
import de.leonhardt.sbm.gui.GuiUtils;
import de.leonhardt.sbm.xml.model.Smses;

/**
 * Background Worker to load the selected XML files.
 * @author Frederik Leonhardt
 *
 */
public class ReadXMLWorker extends SwingWorker<List<Smses>, Smses> {

	private Logger log;
	private File[] files;
	private MessageIO mio;
	
	public ReadXMLWorker(MessageIO mio, File[] files) {
		super();
		this.log = Logger.getLogger(this.getClass().getSimpleName());
		this.mio = mio;
		this.files = files;
	}
	
	@Override
    public List<Smses> doInBackground() {
		setText("Reading XML files..");
		
		List<Smses> smsList = new ArrayList<Smses>();

		// try to read each file
		for (int i = 0; i<files.length; i++) {
			File curFile = files[i];
			try{
				Smses smses = mio.readFromXML(curFile);
				smsList.add(smses);
				//publish(smses);
				// update progress
				Double progress = ((i+1.)/files.length)*100;
				setProgress(progress.intValue());
				setText("Reading XML files.. " + progress + "% - '" + curFile.getPath() + "'");
				// log status
				log.info("[Load] Read " + smses.getCount() +  " messages.");
			// raise any exceptions later as alert dialog in the EDT thread
			} catch (JAXBException e) {
				log.severe("JAXB Error loading file:" + e.toString());
				raiseLater("Error loading file", "Could not load file '" + curFile.toString() + "'." + GuiUtils.BR + "Did you select a valid XML file?");
			} catch (IllegalArgumentException e) {
				log.severe("Error loading file:" + e.toString());
				raiseLater("Error loading file", "Could not load file '" + curFile.toString() +"'.");
			} catch (FaultyInputXMLException e) {
				log.severe("Error loading file:" + e.toString());
				raiseLater("Error loading file", "Could not load file '" + curFile.toString() +"'." + GuiUtils.BR + "No messages found in XML.");
			}
		}
        return smsList;
    }

	@Override
	protected void process(List<Smses> smses) {
//		for (final Smses messagesToImport: smses) {
//			log.info("[Read] Processed " + messagesToImport.getCount() +  " messages.");
//			//bm.importMessages(messagesToImport);
//			
//			SwingWorker importWorker = new SwingWorker<Void, Void>() {
//
//				@Override
//				protected Void doInBackground() throws Exception {
//					bm.importMessages(messagesToImport);
//					return null;
//				}
//			};
//			log.info("Starting sub-worker..");
//			importWorker.execute();
//		}
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
		log.info("[Load] Done.");
		setText("Done!");
    }
    
    /**
     * Fires a property change (text)
     * @param text
     */
    private void setText(String text) {
		firePropertyChange("text", null, text);
    }
    
}
