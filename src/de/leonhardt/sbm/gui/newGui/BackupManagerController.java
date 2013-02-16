package de.leonhardt.sbm.gui.newGui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;

import de.leonhardt.sbm.core.BackupManager;
import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.core.model.Settings;
import de.leonhardt.sbm.core.service.MessageConverterService;
import de.leonhardt.sbm.core.service.MessageIOService;
import de.leonhardt.sbm.gui.common.SettingsDialogView;
import de.leonhardt.sbm.gui.common.SettingsPM;
import de.leonhardt.sbm.gui.common.SettingsService;
import de.leonhardt.sbm.gui.common.resource.FlagLoader;
import de.leonhardt.sbm.gui.common.resource.FlagService;
import de.leonhardt.sbm.gui.common.resource.IconLoader;
import de.leonhardt.sbm.gui.common.resource.IconService;
import de.leonhardt.sbm.smsbr.SmsBrConverter;
import de.leonhardt.sbm.smsbr.SmsBrIO;
import de.leonhardt.sbm.smsbr.xml.model.Sms;

/**
 * The GUI Controller.
 * It is responsible for initialising and linking the presentation models, views and context.
 * 
 * @author Frederik Leonhardt
 *
 */
public class BackupManagerController implements ViewController {

	/**
	 * Starts the GUI
	 * @param args
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BackupManagerController controller = new BackupManagerController();
					controller.showMainFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
    private final BackupManagerPM pm;
    private final BackupManagerService<Sms> service;
    private final BackupManagerView mainView;
    private final SettingsPM spm;
    private final SettingsDialogView sdv;
    
    /**
     * Creates a new BackupmanagerController.
     * - Creates main view + PM
     * - Creates settings view + PM
     * - Creates BackUpManager program instance
     * - Populates Context
     * 
     * @throws MessageIOException
     */
    public BackupManagerController() throws MessageIOException {
    	// create main view and pm
    	mainView = new BackupManagerView();
    	pm = new BackupManagerPM(mainView);
    	
    	// DEBUG
    	/*
    	pm.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				System.out.println("Property change '" + arg0.getPropertyName() + "' from '" +arg0.getOldValue()+ "' to '"+arg0.getNewValue()+"'");
			}
		});
		*/
    	
    	// global services
    	IconService iconService = new IconLoader();
    	FlagService flagService = new FlagLoader();

    	// create settings view and pm
    	sdv = new SettingsDialogView(getMainView());
    	spm = new SettingsPM(sdv); // the pm knows its view to hide it, TODO: use ViewController instead?
    	sdv.setPresentationModel(spm);
    	
    	// create settings context
    	// the SettingsPM needs a SettingsService to manipulate settings
    	// and a FlagService to load the proper flags for the current locale setting.
        spm.getContext().addService(SettingsService.class, Settings.getInstance());
        spm.getContext().addService(FlagService.class, flagService);

    	// create main context
        // The BackupmanagerPM needs a BackupManagerService containing
        // - A MessageService to access messages (implemented by BackupManager)
        // - A ContactService to access contacts (implemented by BackupManager)
        // - A MessageConverterService to convert messages for import and export
        // - A MessageIOService to read and write messages to/from files
        //
        // It also needs an IconLoader to dynamically load icons for messages,
        // a FlagLoader to load flags for contact phone numbers,
        // a ViewController to access important components,
        // and the SettingsService to determine export settings.
//    	DummyService ds = new DummyService();
        BackupManager bm = new BackupManager();
    	MessageConverterService<Sms> mcs = new SmsBrConverter();
    	MessageIOService<Sms> mio = new SmsBrIO(true);
    	service = new BackupManagerService<Sms>(mio, bm, bm, mcs);
    	
        pm.getContext().addService(IconService.class, iconService);
        pm.getContext().addService(FlagService.class, flagService);
        pm.getContext().addService(ViewController.class, this);
    	pm.getContext().addService(BackupManagerService.class, service);
        pm.getContext().addService(SettingsService.class, Settings.getInstance());
    	
    	// assign pm to view
    	mainView.setPresentationModel(pm);
    	
    	// set application icon
    	mainView.setIconImage(iconService.getIcon("sbm-book-open.png").getImage());
    }
    
    /**
     * Makes main frame visible
     */
    public void showMainFrame() {
    	mainView.setVisible(true);
    }
    
    @Override
    public void setDirty() {
    	// refresh main PM
    	pm.refresh();
    }

	@Override
	public Frame getMainView() {
		return this.mainView;
	}

	@Override
	public Component getSettingsView() {
		return sdv;
	}
}
