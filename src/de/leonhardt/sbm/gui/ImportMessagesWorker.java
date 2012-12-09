package de.leonhardt.sbm.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.xml.bind.JAXBException;

import de.leonhardt.sbm.BackupManager;
import de.leonhardt.sbm.MessageIO;
import de.leonhardt.sbm.exception.FaultyInputXMLException;
import de.leonhardt.sbm.xml.model.Smses;

public class ImportMessagesWorker extends SwingWorker<List<Smses>, Smses> {

	private Logger log;
	private BackupManager bm;
	private File[] files;
	private MessageIO mio;
	
	public ImportMessagesWorker(BackupManager bm, MessageIO mio, File[] files) {
		super();
		this.log = Logger.getLogger("ImportMessagesWorker");
		this.mio = mio;
		this.files = files;
		this.bm = bm;
	}
	
	@Override
    public List<Smses> doInBackground() {
		List<Smses> smsList = new ArrayList<Smses>();

		for (int i = 0; i<files.length; i++) {
			File curFile = files[i];
			try{
				Smses smses = mio.readFromXML(curFile);
				smsList.add(smses);
				publish(smses);
				Double progress = ((i+1.)/files.length)*100;
				//log.info("progress " + progress);
				setProgress(progress.intValue());
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
        //bm.importMessages(messagesToImport);
        return smsList;
    }

	@Override
	protected void process(List<Smses> smses) {
		for (Smses messagesToImport: smses) {
			log.info("[Read] Processed " + messagesToImport.getCount() +  " messages.");
			//bm.importMessages(messagesToImport);
		}
	}

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
        try { 
            List<Smses> smses = get();
            for (Smses messagesToImport: smses) {
    			log.info("[Import] Processed " + messagesToImport.getCount() +  " messages.");
    			bm.importMessages(messagesToImport);
    		}
        } catch (Exception ignore) {
        }
    }
}
