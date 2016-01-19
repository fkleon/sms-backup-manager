package de.leonhardt.sbm.gui.newGui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Property;
import org.beanfabrics.support.Service;
import org.beanfabrics.support.Validation;

import de.leonhardt.sbm.core.model.Contact;
import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.core.model.Settings;
import de.leonhardt.sbm.core.util.AppInfo;
import de.leonhardt.sbm.gui.common.ContactPM;
import de.leonhardt.sbm.gui.common.GuiUtils;
import de.leonhardt.sbm.gui.common.MessagePM;
import de.leonhardt.sbm.gui.common.SettingsService;
import de.leonhardt.sbm.gui.common.StatusBarPM;
import de.leonhardt.sbm.gui.common.resource.IconService;
import de.leonhardt.sbm.gui.common.worker.ImportMessagesWorker;
import de.leonhardt.sbm.gui.common.worker.ReadFilesWorker;
import de.leonhardt.sbm.gui.newGui.worker.ExportMessagesWorker;
import de.leonhardt.sbm.gui.newGui.worker.PopulateListWorker;
import de.leonhardt.sbm.smsbr.xml.model.Sms;

/**
 * The main Presentation Model of the BackupManager GUI.
 * 
 * @author Frederik Leonhardt
 *
 */
@SuppressWarnings("unused")
public class BackupManagerPM extends AbstractPM {

	public static final String VERSION = String.format("%s (newGui)%n%s", AppInfo.VERSION, AppInfo.BUILD_TIME);

	// log
	private final Logger log = Logger.getLogger(getClass().getSimpleName());
	
	// menu operations
	private final IOperationPM importMessages = new OperationPM();
	private final IOperationPM exportSelectedMessages = new OperationPM();
	private final IOperationPM exportAllMessages = new OperationPM();
	private final IOperationPM openSettings = new OperationPM();
	private final IOperationPM openAbout = new OperationPM();
	private final IOperationPM openLog = new OperationPM();

	// other operations
	// TODO: edit contact (right-click event?)
	
	// properties
	private final ListPM<ContactPM> contacts = new ListPM<ContactPM>(50);
	private final ListPM<MessagePM> currentMessages = new ListPM<MessagePM>(500);
	
	private final IntegerPM messageCount = new IntegerPM(0);
	private final IntegerPM contactCount = new IntegerPM(0);
	
	// sub-PMs
	// views should share same ModelProvider to share context as well
	private final StatusBarPM statusBar = new StatusBarPM();
	
	// log text
	// TODO: LogPM? Or own view..
	
	// services
	private BackupManagerService<Sms> bms;
	private SettingsService ss;
	private ViewController vc;
	
	// contact selection
	private int selectedContactIndex;
	private int selectedMessageIndex;
	
	// other stuff
	private final JFrame rootFrame;
	private JFileChooser fileChooserLoad;
	private JFileChooser fileChooserSave;
	private SwingWorker<?,?> populateWorker;

	/**
	 * Creates a new presentation model for the BackupManager.
	 * @param rootFrame
	 */
	public BackupManagerPM(JFrame rootFrame) {
		this.rootFrame = rootFrame;
		this.messageCount.setMandatory(false);
		this.contacts.getValidator().clear(); //TODO double check
		PMManager.setup(this);

		initFileChooser();
	}
	
	/**
	 * Initialises the two file choosers for loading and saving.
	 */
	public void initFileChooser() {
		// FILE CHOOSER
		fileChooserLoad = new JFileChooser();
		fileChooserLoad.setMultiSelectionEnabled(true);
		fileChooserLoad.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));

		fileChooserSave = new JFileChooser();
		fileChooserSave.setMultiSelectionEnabled(false);
		fileChooserSave.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));
	}
	
	/**
	 * Refreshes the contact list and the current message selection.
	 */
	public void refresh() {
		if (bms != null) {
			setContacts(bms.getContactService().getContacts());
			setMessagesBySelection();
		} else {
			log.warning("Can not refresh, no BackupManagerService specified.");
		}
	}
	
	@Service
	public void setService(BackupManagerService<Sms> bms) {
		this.bms = bms;
		this.refresh();
	}
	
	@Service
	public void setService(ViewController vc) {
		this.vc = vc;
	}
	
	@Service
	public void setService(SettingsService ss) {
		this.ss = ss;
	}
	
	/**
	 * Clears the current contact list and inserts the given contacts.
	 * Updates contactCount.
	 * @param contacts
	 */
	private void setContacts(Collection<Contact> contacts) {
		this.contacts.clear();
		
		Collection<ContactPM> contactPMs = new ArrayList<>();
		for (Contact c: contacts) {
			ContactPM cPM = new ContactPM(c);
			contactPMs.add(cPM);
		}
		
		this.contacts.addAll(contactPMs);

		// update contact and message count
		contactCount.setInteger(this.contacts.size());
		messageCount.setInteger(bms.getMessageService().getMessageCount());
	}
	
	/**
	 * Returns the selected Contact object.
	 * @return
	 */
	private Contact getSelectedContact() {
		ContactPM cPM = contacts.getSelection().getFirst();
		
		if (cPM != null) {
			return new Contact(cPM.contactName.getText(), cPM.addressIntl.getText());
		} else {
			return null;
		}
	}
	
	/**
	 * Checks if the message list is currently being rendered.
	 * @return true, if list is not busy
	 */
	@Validation(path={"exportSelectedMessages","importMessages"})
	public boolean isNotRendering() {
		if (populateWorker != null) {
			return populateWorker.isDone();
		} else {
			return true;
		}
	}
	
	/**
	 * Refreshes the displayed messages according to currently selected contact.
	 * Updates selectedContactIndex.
	 */
	@OnChange(path="contacts")
	public void setMessagesBySelection() {
		int newSelection = contacts.getSelection().getMinIndex();

		if (!isNotRendering() || newSelection == selectedContactIndex) {
			// busy, reset selection
			contacts.getSelection().setInterval(selectedContactIndex, selectedContactIndex);
			return;
		}
		
		// update selection
		selectedContactIndex = newSelection;
		
		// get selected contact
		Contact selectedContactObject = getSelectedContact();

		if (selectedContactObject != null) {
			// clear message list
			currentMessages.clear();
			selectedMessageIndex = 0;
			
			// get conversation with selected contact
			Collection<Message> messages = bms.getMessageService().getMessages(selectedContactObject);
			IconService iconService = getContext().getService(IconService.class);

			// start worker for list population
			populateWorker = new PopulateListWorker(messages, currentMessages, iconService);
			addFeedbackListeners(populateWorker);
			
			populateWorker.execute();
		}
	}
	
	@OnChange(path="currentMessages")
	//TODO this doesnt do much..
	public void test() {
		int newSelection = currentMessages.getSelection().getMinIndex();

		if (newSelection == selectedMessageIndex) {
			// ignore
			return;
		}
		
		// update selection
		selectedMessageIndex = newSelection;
		
		//TODO?
	}
	
	/**
	 * Opens file chooser and handles message import.
	 */
	@Operation(path="importMessages")
	public void importMessages() {
		// open file chooser dialog
		int ret = fileChooserLoad.showOpenDialog(rootFrame);
					
		if (!(ret == JFileChooser.APPROVE_OPTION)) {
			// if user did cancel selection, return
			return;
		}
		
		// read all selected files
		File[] selection = fileChooserLoad.getSelectedFiles();
		
		// setup SwingWorker threads
		SwingWorker<?,?> loadWorker = new ReadFilesWorker<Sms>(bms.getMessageIO(), selection); // loads the given files
		SwingWorker<?,?> importWorker = new ImportMessagesWorker<Sms>(bms.getMessageService(), bms.getConverterService(), vc); // imports the messages afterwards
		addFeedbackListeners(loadWorker);
		addFeedbackListeners(importWorker);
		loadWorker.addPropertyChangeListener((PropertyChangeListener)importWorker); // the import worker must be notified when loadWorker is done

		// start work
		loadWorker.execute();
	}
	
	/**
	 * Adds feedback listeners to given SwingWorker:
	 * - statusBar (progress bar + status text)
	 * - WaitCursor (cursor)
	 * 
	 * @param sw
	 */
	private void addFeedbackListeners(SwingWorker<?,?> sw) {
		sw.addPropertyChangeListener((PropertyChangeListener)statusBar);
		sw.addPropertyChangeListener(new WaitCursorListener());
	}
	
	/**
	 * Checks if messages can be imported.
	 * This is only possible, if the BMS service is set.
	 * @return
	 */
	@Validation(path="importMessages")
	public boolean canImport() {
		return (bms != null);
	}
	
	@Operation(path="exportSelectedMessages")
	public void exportSelectedMessages() {
		export(true);
	}
	
	@Operation(path="openLog")
	public void openLog() {
		//TODO
	}
	
	/**
	 * Export is available, when BackupManagerService is set and contact list is not empty.
	 * @return
	 */
	@Validation(path = {"exportSelectedMessages", "exportAllMessages"})
	public boolean canExport() {
		return (!contacts.isEmpty() && bms != null);
	}
	
	/**
	 * Export selected is available, when currentMessages is not empty.
	 * @return
	 */
	@Validation(path = "exportSelectedMessages")
	public boolean canExportSelected() {
//		return (!currentMessages.isEmpty());
		return (this.currentMessages != null && this.currentMessages.size() > 0);
	}
	
	/**
	 * Exports all messages in store.
	 */
	@Operation
	public void exportAllMessages() {
		export(false);
	}
	
	/**
	 * Generates a filename for export dialog
	 * @return
	 */
	private String getFileName() {
		 String df = new SimpleDateFormat("yyyy-MM-dd-HHmm").format(new Date());
		 return String.format("export-%s.xml", df);
	}
	
	/**
	 * Gets export settings from SettingsService and delegates work.
	 * @param selectedOnly
	 */
	private void export(boolean selectedOnly) {
		Settings settings = ss.getSettings();
		boolean keepOriginals = !settings.getExportInternationalNumbers();
		boolean exportDupes = settings.isExportDupes();
		
		export(selectedOnly, keepOriginals, exportDupes);
	}
	
	/**
	 * Handles message export operations:
	 * - Opens file selection menu and handles return value
	 * - Asks for confirmation if file already exists
	 * - Determines which messages have to be exported (selectedOnly or all)
	 * - Delegates the the messages and selected file to MessageIO for writing
	 * 
	 * @param selectedOnly, if only selected conversation should be exported
	 * @param keepOriginals, if original number format and name should be kept
	 * @param exportDupes, if duplicates should be exported
	 */
	private void export(boolean selectedOnly, boolean keepOriginals, boolean exportDupes) {
		// select default file name
		if (fileChooserSave.getSelectedFile() == null) {
			fileChooserSave.setSelectedFile(new File(getFileName()));
		}

		// open file chooser dialog
		int ret = fileChooserSave.showSaveDialog(rootFrame);
				
		if (!(ret == JFileChooser.APPROVE_OPTION)) {
			// if user did cancel selection, return
			return;
		}
				
		// read selected file
		File f = fileChooserSave.getSelectedFile();
		
		// check if file already exists
		if (f.exists()) {
			int selRet = GuiUtils.alertSelection(rootFrame, "Confirm Export", "'" + f.getPath() + "' already exists." + GuiUtils.BR + "Do you want to replace it?");
			
			// ret -1 = closed by X, 0 = YES, 1 = NO, 2 = CANCEL
			if (selRet >= 1) {
				return;
			}
		}
		
		// setup SwingWorker thread
		SwingWorker<?,?> exportWorker = new ExportMessagesWorker<Sms>(f, bms, keepOriginals, exportDupes, selectedOnly?getSelectedContact():null);
		addFeedbackListeners(exportWorker);
		
		// go!
		exportWorker.execute();
	}
	
	/**
	 * Opens the settings dialog
	 */
	@Operation(path="openSettings")
	public void openSettings() {
		if (vc != null) {
			vc.getSettingsView().setVisible(true);
		} else {
			log.warning("Can not open settings, no ViewController specified.");
		}
	}
	
	/**
	 * Opens about dialog
	 */
	@Operation(path="openAbout")
	public void openAbout() {
		GuiUtils.alertInfo(rootFrame,"BackupManager",
				VERSION
				+ GuiUtils.BR + GuiUtils.BR + "by Frederik Leonhardt"
				+ GuiUtils.BR + "<frederik.leonhardt@gmail.com>"
				+ GuiUtils.BR + GuiUtils.BR + "Flag icons by FamFamFam.com"
				+ GuiUtils.BR + "Message icons by GLYPHICONS.com");
	}
	
	/**
	 * This listener modifies the current mouse cursor depending on the state
	 * of the attached object.
	 * 
	 * @author Frederik Leonhardt
	 *
	 */
	private class WaitCursorListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// automatically capture state information
			if ("state".equals(evt.getPropertyName())) {
	            if (SwingWorker.StateValue.STARTED == evt.getNewValue()) {
	            	// start wait cursor
	                GuiUtils.startWaitCursor(rootFrame);
	            }
	            if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
	            	// stop wait cursor
	                GuiUtils.stopWaitCursor(rootFrame);
	            }
	        }
		}
	}
}
