package de.leonhardt.sbm.gui.oldGui;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.SwingWorker;

import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import de.leonhardt.sbm.core.BackupManager;
import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.core.model.Contact;
import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.core.model.Settings;
import de.leonhardt.sbm.core.service.MessageConverterService;
import de.leonhardt.sbm.gui.common.GuiUtils;
import de.leonhardt.sbm.gui.common.SettingsDialogView;
import de.leonhardt.sbm.gui.common.SettingsPM;
import de.leonhardt.sbm.gui.common.SettingsService;
import de.leonhardt.sbm.gui.common.StatusBarPM;
import de.leonhardt.sbm.gui.common.resource.FlagLoader;
import de.leonhardt.sbm.gui.common.resource.FlagService;
import de.leonhardt.sbm.gui.common.resource.IconLoader;
import de.leonhardt.sbm.gui.common.worker.ReadFilesWorker;
import de.leonhardt.sbm.gui.oldGui.renderer.ContactListCellRenderer;
import de.leonhardt.sbm.gui.oldGui.renderer.CustomListModel;
import de.leonhardt.sbm.gui.oldGui.renderer.MessageListCellRenderer;
import de.leonhardt.sbm.gui.oldGui.worker.ImportMessagesWorkerOld;
import de.leonhardt.sbm.smsbr.SmsBrConverter;
import de.leonhardt.sbm.smsbr.SmsBrIO;
import de.leonhardt.sbm.smsbr.xml.model.Sms;
import de.leonhardt.sbm.smsbr.xml.model.Smses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.io.File;
import java.awt.GridLayout;
import java.awt.Color;
import java.beans.PropertyChangeListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The old GUI, mostly without beanfabrics.
 * 
 * @author Frederik Leonhardt
 *
 */
public class BackupManagerGUI {

	private Logger log = Logger.getLogger("BackupManagerGUI");
	private static String VERSION_INFO = "v0.9beta-oldGui (2012-12-18)";
	
	private BackupManager bm;
	private SmsBrIO mio;
	private MessageConverterService<Sms> msgConv;
		
	private JFrame frmBackupManager;
	private JDialog dlgSettings;
	private JList listConversations;
	private JList listMessages;
	private JFileChooser fileChooserLoad;
	private JFileChooser fileChooserSave;
	private JTextPane logPane;
	private JTextPane messagePane;
	private StatusBarPM statusBarModel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BackupManagerGUI window = new BackupManagerGUI();
					window.frmBackupManager.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BackupManagerGUI() {
		// init program logic
		try {
			initializeLogic();
		} catch (MessageIOException e) {
			GuiUtils.alertError(frmBackupManager,"Error","Initialization failed: " + e.getMessage()); //TODO can ou actually display before GUI init?
			return;
		}
		
		// init GUI
		initialize();
		
		// init logging
		initializeLogging();
		
	}
	
	private void initializeLogging() {
		// add handler for GUI element to root logger
		Logger.getLogger("").addHandler(new CustomLogHandler(getlogPane()));
		
		// add file handler
		/*
		try {
			FileHandler handler = new FileHandler("BackupManager.log");
			Logger.getLogger("").addHandler(handler);
		} catch (Exception e) {
			log.warning("Could not initialize logging to file!");
		}
		*/
	}

	private void initializeLogic() throws MessageIOException {
		// load prefs
		Settings set = Settings.getInstance();
		
		// init business logic
		this.bm = new BackupManager();
		this.mio = new SmsBrIO(true);
		this.msgConv = new SmsBrConverter();
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// FILE CHOOSER
		fileChooserLoad = new JFileChooser();
		fileChooserLoad.setMultiSelectionEnabled(true);
		fileChooserLoad.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));

		fileChooserSave = new JFileChooser();
		fileChooserSave.setMultiSelectionEnabled(false);
		fileChooserSave.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));
		
		// MAIN FRAME
		frmBackupManager = new JFrame();
		frmBackupManager.setTitle("BackupManager");
		frmBackupManager.setBounds(100, 100, 900, 600);
		frmBackupManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// SETTINGS DIALOG
		SettingsDialogView sdv = new SettingsDialogView(frmBackupManager);
		SettingsPM spm = new SettingsPM(sdv);
		sdv.setPresentationModel(spm);
        spm.getContext().addService(FlagService.class, new FlagLoader());
        spm.getContext().addService(SettingsService.class, Settings.getInstance());
        //spm.setSettings(Settings.getInstance());
        
		dlgSettings = sdv;
		//dlgSettings = new SettingsDialog(frmBackupManager, true, set);
		// DUPLICATE DIALOG
		//dlgDuplicates = new DuplicateDialog();
		
		// MENU BAR and MENUS
		JMenuBar menuBar = new JMenuBar();
		frmBackupManager.setJMenuBar(menuBar);
		
		JMenu mnMessages = new JMenu("Messages");
		menuBar.add(mnMessages);
		
		JMenuItem mntmImport = new JMenuItem("Import");
		mntmImport.addActionListener(new ImportActionListener());
		mnMessages.add(mntmImport);
		
		JMenuItem mntmExportAll = new JMenuItem("Export all");
		mntmExportAll.addActionListener(new ExportActionListener(false));
		mnMessages.add(mntmExportAll);
		
		JMenuItem mntmExportSel = new JMenuItem("Export selected");
		mntmExportSel.addActionListener(new ExportActionListener(true));
		mnMessages.add(mntmExportSel);
		
		JSeparator separator = new JSeparator();
		mnMessages.add(separator);
		
		JMenuItem mntmSettings = new JMenuItem("Settings");
		mntmSettings.addActionListener(new ActionListener() {
			
			private Settings set = Settings.getInstance();
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// open dialog
				dlgSettings.setVisible(true);
				
				// TODO check returnValue to determine if we need to re-initialize
				// TODO actually, this should be solved by listener in Settings or something
				//bm.initLocale(set.getCountryCode(), set.getLanguageCode(), set.getRegionCode());
			}
		});
		mnMessages.add(mntmSettings);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new AboutActionListener());
		mnHelp.add(mntmAbout);
		
		/*
		 *  CONTENT
		 *  __ __
		 * |  |  |
		 * |__|__|
		 * |_____|
		 *  
		 */
		//frmBackupManager.getContentPane().setLayout(new GridLayout(0,1));
		frmBackupManager.getContentPane().setLayout(new BorderLayout());
		
		// alternative
		JPanel mainPanel = new JPanel(new GridLayout(0,1));
		
		/*
		 * Split pane centered
		 *  __ __
		 * |  |  |
		 * |__|__|
		 */
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0.3);
		//frmBackupManager.getContentPane().add(splitPane, BorderLayout.CENTER);
		mainPanel.add(splitPane);
		
		/*
		 * Conversation list to the left of split pane,
		 * sitting on a scroll pane
		 */
		listConversations = new JList();
		listConversations.setCellRenderer(new ContactListCellRenderer());
		listConversations.setVisibleRowCount(0);
		listConversations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listConversations.addListSelectionListener(new ContactListSelectionListener());
		listConversations.setModel(getDefaultListModel("Import your backup."));
		JScrollPane scrollPaneContacts = new JScrollPane(listConversations);
		
		/*
		 * Message output to the right of the split pane,
		 * sitting on a scroll pane
		 */
		listMessages = new JList();
		listMessages.setCellRenderer(new MessageListCellRenderer());
		listMessages.setVisibleRowCount(0);
		listMessages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//listMessages.addListSelectionListener(new ContactListSelectionListener());
		JScrollPane scrollPaneMessages = new JScrollPane(listMessages);
		//scrollPaneMessages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		/*
		messagePane = new JTextPane();		
		messagePane.setEditable(false);
		messagePane.setText("Import a Message Backup to view your messages here..");
		
		JScrollPane scrollPaneMessages = new JScrollPane(messagePane);
		*/
		
		splitPane.add(scrollPaneContacts);
		splitPane.add(scrollPaneMessages);
		
		
		/*
		 * Logging output under split pane,
		 * sitting on a scroll pane
		 */
		logPane = new JTextPane();
		logPane.setPreferredSize(new Dimension(0, 150)); // at least 150px high
		logPane.setBackground(new Color(255, 240, 220));
		logPane.setEditable(false);
		logPane.setText("Logging output..");
		
		JScrollPane scrollPaneLog = new JScrollPane(logPane);
		//frmBackupManager.getContentPane().add(scrollPaneLog, BorderLayout.SOUTH);	
		mainPanel.add(scrollPaneLog);
		
		/*
		 * Status bar
		 */
		//statusBar = new StatusBar();
		statusBarModel = new StatusBarPM();
		StatusBarView statusBarView = new StatusBarView();
		statusBarView.setPresentationModel(statusBarModel);
		
        //spm.setSettings(Settings.getInstance());
        //spm.getContext().addService(SettingsService.class, Settings.getInstance());
		
		// add main panel and status bar to main frame
		frmBackupManager.add(mainPanel, BorderLayout.CENTER);
		frmBackupManager.add(statusBarView, BorderLayout.SOUTH);
	}
	
	protected ListModel<Object> getDefaultListModel(String entry) {
		DefaultListModel<Object> dlm = new DefaultListModel<>();
		if (entry !=null) {
			dlm.addElement(entry);
		}
		return dlm;
	}

	protected JList getListConversations() {
		return listConversations;
	}
	protected JList getListMessages() {
		return listMessages;
	}
	protected JTextPane getlogPane() {
		return logPane;
	}
	protected JTextPane getMessagePane() {
		return messagePane;
	}
	protected JFileChooser getFileChooserLoad() {
		return fileChooserLoad;
	}
	
	/*
	 * SUBCLASSES
	 * ACTIONLISTENER
	 */
	
	/**
	 * Handles file imports:
	 * - Opens file selection menu and handles return value
	 * - Delegates the the selected files to MessageIO for reading
	 * - Imports all Messages into BackupManager
	 * 
	 * @author Frederik Leonhardt
	 *
	 */
	public class ImportActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// open file chooser dialog
			int ret = fileChooserLoad.showOpenDialog(frmBackupManager);
			
			if (!(ret == JFileChooser.APPROVE_OPTION)) {
				// if user did cancel selection, return
				return;
			}
			
			// read all selected files
			File[] selection = fileChooserLoad.getSelectedFiles();
			List<Smses> smsList = new ArrayList<Smses>();
			
			ImageIcon load_anim = new IconLoader().getLoadingAnimation();
			
			statusBarModel.setLoadingIcon(load_anim);
			//statusBarModel.setStatus("Importing messages..", load_anim, 0);
//			statusBar.setText("Importing messages..");
			
			CustomListModel dlm = new CustomListModel();
			listConversations.setModel(dlm);

			SwingWorker loadWorker = new ReadFilesWorker(mio, selection);
			SwingWorker importWorker = new ImportMessagesWorkerOld<Sms>(bm, msgConv, dlm);
			loadWorker.addPropertyChangeListener((PropertyChangeListener)statusBarModel);
			importWorker.addPropertyChangeListener((PropertyChangeListener)statusBarModel);
			loadWorker.addPropertyChangeListener((PropertyChangeListener)importWorker);
			
			// start work
			loadWorker.execute();
		}
	}
	
	/**
	 * Constructor takes one argument. If true, only the selected conversation will be exported.
	 * 
	 * Handles message export operations:
	 * - Opens file selection menu and handles return value
	 * - Asks for confirmation if file already exists
	 * - Determines which messages have to be exported (selectedOnly or all)
	 * - Delegates the the messages and selected file to MessageIO for writing
	 * 
	 * @author Frederik Leonhardt
	 *
	 */
	public class ExportActionListener implements ActionListener {

		private boolean selectedOnly;
		
		public ExportActionListener(boolean selectedOnly) {
			this.selectedOnly = selectedOnly;
		}
		
		private String getFileName() {
			 String df = new SimpleDateFormat("yyyy-MM-dd-HHmm").format(new Date());
			 return String.format("export-%s.xml", df);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// select default file name
			if (fileChooserSave.getSelectedFile() == null) {
				fileChooserSave.setSelectedFile(new File(getFileName()));
			}
			
			// open file chooser dialog
			int ret = fileChooserSave.showSaveDialog(frmBackupManager);
			
			if (!(ret == JFileChooser.APPROVE_OPTION)) {
				// if user did cancel selection, return
				return;
			}
			
			// read all selected files
			File f = fileChooserSave.getSelectedFile();
			
			// check if file already exists
			if (f.exists()) {
				int selRet = GuiUtils.alertSelection(frmBackupManager, "Confirm Export", "'" + f.getPath() + "' already exists." + GuiUtils.BR + "Do you want to replace it?");
				
				// ret -1 = closed by X, 0 = YES, 1 = NO, 2 = CANCEL
				if (selRet >= 1) {
					return;
				}
			}
			
			Smses smses = null;
			boolean keepOrig = !Settings.getInstance().getExportInternationalNumbers();
			boolean exportDupes = Settings.getInstance().isExportDupes();
			
			// determine which messages to export
			if (!selectedOnly) {
				// all messages
				smses = new Smses(msgConv.toExternalCol(bm.getMessages(),keepOrig,exportDupes));
			} else {
				// get selected contact
				// get selected index directly from list
				int index = listConversations.getSelectedIndex();
				
				if (index < 0 || index > listConversations.getModel().getSize()) {
					// no selection
					GuiUtils.alertInfo(frmBackupManager,"Export", "No conversation selected.");
					return;
				}
				
				// get messages for contact
				Object o = listConversations.getModel().getElementAt(index);
				
				if (o instanceof Contact) {
					Contact c = (Contact) o;
					smses = new Smses(msgConv.toExternalCol(bm.getMessages(c),keepOrig,exportDupes));
				} else {
					GuiUtils.alertInfo(frmBackupManager,"Export", "No conversation selected.");
					return;
				}
			}
		
			// try exporting the stuff
			try {
				mio.writeTo(smses.getSms(), f);
				//alertInfo("Export sucessfull", "Export successfull.");
			} catch (IllegalArgumentException e1) {
				log.severe("Error writing file:" + e1.toString());
				GuiUtils.alertError(frmBackupManager,"Error while exporting messages", "Could not write to file '" + f.toString() +"'.");
			} catch (MessageIOException e1) {
				log.severe("Error writing file (selectedOnly: '" + selectedOnly + "'): " + e1.toString());
				if (selectedOnly) {
					GuiUtils.alertError(frmBackupManager,"Error while exporting messages", "There is a problem exporting your messages." + GuiUtils.BR + "Does your selection contain at least one message?");
				} else {
					GuiUtils.alertError(frmBackupManager,"Error while exporting messages", "There is a problem exporting your messages." + GuiUtils.BR + "Did you import at least one message?");
				}
			}
		}
		
	}
	
	/**
	 * Displays little 'about' info box.
	 * 
	 * @author Frederik Leonhardt
	 *
	 */
	public class AboutActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			GuiUtils.alertInfo(frmBackupManager,"BackupManager",
										VERSION_INFO
										+ GuiUtils.BR + GuiUtils.BR + "by Frederik Leonhardt"
										+ GuiUtils.BR + "<frederik.leonhardt@gmail.com>"
										+ GuiUtils.BR + GuiUtils.BR + "Flag icons by FamFamFam.com"
										+ GuiUtils.BR + "Message icons by GLYPHICONS.com");
		}
	}
	
	/**
	 * Handles contact selections:
	 * - Determines which contact is selected
	 * - Opens conversation for contact
	 * 
	 * @author Frederik Leonhardt
	 *
	 */
	public class ContactListSelectionListener implements ListSelectionListener {
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			// determine if event is part of a event row
			if (e.getValueIsAdjusting()) {
				return;
			}
			
			// get selected index directly from list
			int index = listConversations.getSelectedIndex();
			
			if (index < 0 || index > listConversations.getModel().getSize()) {
				// no selection
				listMessages.setModel(getDefaultListModel("Please select a contact to display the messages."));
				return;
			}

			// get selected contact
			Object o = listConversations.getModel().getElementAt(index);
			
			if (o instanceof Contact) {
				// and load the messages
				Contact c = (Contact) o;
				Collection<Message> messages = bm.getMessages(c);
				
				log.info(messages.size() + " messages in conversation with '" + c.getContactName() + " <" + c.getAddressIntl() + ">'.");
				
				// populate message list
				CustomListModel dlm = new CustomListModel();
				listMessages.setModel(dlm);

				dlm.addElements(messages);
			}
		}
	}
	
}
