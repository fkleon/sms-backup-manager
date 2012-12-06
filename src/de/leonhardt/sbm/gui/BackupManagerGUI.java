package de.leonhardt.sbm.gui;

import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;

import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.xml.bind.JAXBException;

import de.leonhardt.sbm.BackupManager;
import de.leonhardt.sbm.MessageIO;
import de.leonhardt.sbm.exception.FaultyInputXMLException;
import de.leonhardt.sbm.gui.handler.CustomLogHandler;
import de.leonhardt.sbm.gui.renderer.ContactListCellRenderer;
import de.leonhardt.sbm.gui.renderer.CustomListModel;
import de.leonhardt.sbm.gui.renderer.MessageListCellRenderer;
import de.leonhardt.sbm.util.Utils;
import de.leonhardt.sbm.util.Utils.TimeTracker;
import de.leonhardt.sbm.xml.model.Contact;
import de.leonhardt.sbm.xml.model.Sms;
import de.leonhardt.sbm.xml.model.Smses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.io.File;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ScrollPaneConstants;

public class BackupManagerGUI {

	private Logger log = Logger.getLogger("BackupManagerGUI");
	private static String VERSION_INFO = "v0.6 (2012-12-06)";
	
	private BackupManager bm;
	private MessageIO mio;
	
	private JFrame frmBackupManager;
	private JList listConversations;
	private JList listMessages;
	private JFileChooser fileChooserLoad;
	private JFileChooser fileChooserSave;
	private JTextPane logPane;
	private JTextPane messagePane;
	
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
		} catch (JAXBException e) {
			alertError("Error","Initialization failed: " + e.getMessage()); //TODO can ou actually display before GUI init?
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

	private void initializeLogic() throws JAXBException {
		this.bm = new BackupManager();
		this.mio = new MessageIO(true);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// FILE CHOOSER
		fileChooserLoad = new JFileChooser();
		fileChooserLoad.setMultiSelectionEnabled(true);
		
		fileChooserSave = new JFileChooser();
		fileChooserSave.setMultiSelectionEnabled(false);
		fileChooserSave.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));
		
		// MAIN FRAME
		frmBackupManager = new JFrame();
		frmBackupManager.setTitle("BackupManager");
		frmBackupManager.setBounds(100, 100, 600, 600);
		frmBackupManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		
		JMenuItem mntmShowDuplicates = new JMenuItem("Show Duplicates");
		mnMessages.add(mntmShowDuplicates);
		
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
		frmBackupManager.getContentPane().setLayout(new GridLayout(0,1));
		
		/*
		 * Split pane centered
		 *  __ __
		 * |  |  |
		 * |__|__|
		 */
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0.3);
		frmBackupManager.getContentPane().add(splitPane, BorderLayout.CENTER);
		
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
		JScrollPane scrollPaneContacts = new JScrollPane(listConversations) {
			/*
			@Override
			public Dimension getPreferredSize() {
				return this.getComponent(0).getPreferredSize();
			}
			
			@Override
			public Dimension getMinimumSize() {
				return this.getPreferredSize();
			}
			*/
		};
		
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
		frmBackupManager.getContentPane().add(scrollPaneLog, BorderLayout.SOUTH);	
	}
	
	protected ListModel getDefaultListModel(String entry) {
		DefaultListModel dlm = new DefaultListModel();
		if (entry !=null) {
			dlm.addElement(entry);
		}
		return dlm;
	}
	
	private void alertError(String title, Object msg) {
		String msgTitle = title == null ? "" : title;
		JOptionPane.showMessageDialog(frmBackupManager, msg, msgTitle, JOptionPane.ERROR_MESSAGE);
	}
	
	private void alertInfo(String title, Object msg) {
		String msgTitle = title == null ? "" : title;
		JOptionPane.showMessageDialog(frmBackupManager, msg, msgTitle, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private int alertSelection(String title, Object msg) {
		String msgTitle = title == null ? "" : title;
		return JOptionPane.showConfirmDialog(frmBackupManager, msg, msgTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
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
			
			for (File f: selection) {
				try {
					Smses smses = mio.readFromXML(f);
					smsList.add(smses);
				} catch (JAXBException e) {
					log.severe("JAXB Error loading file:" + e.toString());
					alertError("Error loading file", "Could not load file '" + f.toString() + "'." + GuiUtils.BR + "Did you select a valid XML file?");
				} catch (IllegalArgumentException e) {
					log.severe("Error loading file:" + e.toString());
					alertError("Error loading file", "Could not load file '" + f.toString() +"'.");
				} catch (FaultyInputXMLException e) {
					log.severe("Error loading file:" + e.toString());
					alertError("Error loading file", "Could not load file '" + f.toString() +"'." + GuiUtils.BR + "No messages found in XML.");
				}
			}
			
			// import all messages
			for (Smses smses: smsList) {
				bm.importMessages(smses);
			}
			
			// populate contact list
			CustomListModel dlm = new CustomListModel();
			listConversations.setModel(dlm);
			//dlm.ensureCapacity(bm.getContacts().size()); 

			for (Contact c: bm.getContacts()) {
				dlm.addElement(c);
			}
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
		
		@Override
		public void actionPerformed(ActionEvent e) {
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
				int selRet = alertSelection("Confirm Export", "'" + f.getPath() + "' already exists." + GuiUtils.BR + "Do you want to replace it?");
				
				// ret -1 = closed by X, 0 = YES, 1 = NO, 2 = CANCEL
				if (selRet >= 1) {
					return;
				}
			}
			
			Smses smses = null;
			
			// determine which messages to export
			if (!selectedOnly) {
				// all messages
				List<Sms> msList = new ArrayList<Sms>(bm.getMS());
				smses = new Smses(msList);
			} else {
				// get selected contact
				// get selected index directly from list
				int index = listConversations.getSelectedIndex();
				
				if (index < 0 || index > listConversations.getModel().getSize()) {
					// no selection
					alertInfo("Export", "No conversation selected.");
					return;
				}
				
				// get messages for contact
				Object o = listConversations.getModel().getElementAt(index);
				
				if (o instanceof Contact) {
					Contact c = (Contact) o;
					smses = new Smses(new ArrayList<Sms>(bm.getMessages(c)));
				} else {
					alertInfo("Export", "No conversation selected.");
					return;
				}
			}
		
			// try exporting the stuff
			try {
				mio.writeToXML(smses, f);
				//alertInfo("Export sucessfull", "Export successfull.");
			} catch (IllegalArgumentException e1) {
				log.severe("Error writing file:" + e1.toString());
				alertError("Error while exporting messages", "Could not write to file '" + f.toString() +"'.");
			} catch (JAXBException e1) {
				log.severe("Error writing file (selectedOnly: '" + selectedOnly + "'): " + e1.toString());
				if (selectedOnly) {
					alertError("Error while exporting messages", "There is a problem exporting your messages." + GuiUtils.BR + "Does your selection contain at least one message?");
				} else {
					alertError("Error while exporting messages", "There is a problem exporting your messages." + GuiUtils.BR + "Did you import at least one message?");
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
			alertInfo("BackupManager "+VERSION_INFO, "by Frederik Leonhardt"
										+ GuiUtils.BR+"<frederik.leonhardt@gmail.com>"
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
				Collection<Sms> messages = bm.getMessages(c);
				
				log.info(messages.size() + " messages in conversation with '" + c.getContactName() + "<" + c.getAddressIntl() + ">'.");
				
				// populate message list
				CustomListModel dlm = new CustomListModel();
				listMessages.setModel(dlm);

				dlm.addElements(messages);
			}
		}
	}
}
