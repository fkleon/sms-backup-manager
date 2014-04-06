package de.leonhardt.sbm.gui.newGui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnLabel;
import org.beanfabrics.swing.list.BnList;

import de.leonhardt.sbm.gui.common.ContactPM;
import de.leonhardt.sbm.gui.common.MessagePM;
import de.leonhardt.sbm.gui.newGui.renderer.ContactPMListCellRenderer;
import de.leonhardt.sbm.gui.newGui.renderer.MessagePMListCellRenderer;

/**
 * The main frame of the BackupManager GUI.
 * 
 * It contains
 * - a BackupManagerMenubar,
 * - a main panel,
 * - a BackupManagerStatusBar.
 * 
 *  _______
 * |_______| menuBar
 * |       | mainPanel (see getMainPanel)
 * |_______|
 * |_______| statusBar
 * 
 * @author Frederik Leonhardt
 *
 */
public class BackupManagerView extends JFrame implements View<BackupManagerPM>, ModelSubscriber {

	private static final long serialVersionUID = 5997931113667405119L;

	private BackupManagerMenuBar menuBar;
	private JPanel mainPanel;
	private BackupManagerStatusBar statusBar;
	
	/**
	 * Creates a new BackupManagerFrame
	 */
	public BackupManagerView() {
		super("SMS Backup Manager");
		
		// configure frame itself
		setTitle("SMS Backup Manager");
		setBounds(100, 100, 900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// set layout
		getContentPane().setLayout(new BorderLayout());

		// add menu bar
		menuBar = new BackupManagerMenuBar();
		menuBar.setPath(new Path("this")); // link to backupManagerPM path
		menuBar.setModelProvider(getLocalModelProvider()); // provided by local model provider
		setJMenuBar(menuBar);

		// add main panel
		mainPanel = getMainPanel();
		add(mainPanel, BorderLayout.CENTER);
		
		// add status bar
		statusBar = new BackupManagerStatusBar();
		statusBar.setPath(new Path("this.statusBar")); // link to statusBarPM
		statusBar.setModelProvider(getLocalModelProvider()); // provided by local model provider
		add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Returns the main panel of the application.
	 * @return
	 */
	private JPanel getMainPanel() {
		/*
		 *  CONTENT
		 *  _____
		 * |__ __| Header: messageCount etc
		 * |  |  | Left:  Contact list
		 * |__|__| Right: Message list
		 * |_____| Footer: log
		 *  
		 */
		JPanel mainPanel = new JPanel(new BorderLayout());

		/*
		 * Header NORTH
		 *  _______________
		 * |<ctNo>  <msgNo>|
		 * |_______________|
		 */
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.LINE_AXIS));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		BnLabel contactCountLabel = new BnLabel(getLocalModelProvider(), new Path("this.contactCount"));
		BnLabel messageCountLabel = new BnLabel(getLocalModelProvider(), new Path("this.messageCount"));

		headerPanel.add(new JLabel("Contacts: "));
		headerPanel.add(contactCountLabel);
		headerPanel.add(Box.createHorizontalGlue());
		headerPanel.add(new JLabel("Total (unique) messages: "));
		headerPanel.add(messageCountLabel);
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		
		/*
		 * Split pane CENTER
		 *  __ __
		 * |  |  | left: contact list
		 * |__|__| right: message list
		 */
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0.3);
		mainPanel.add(splitPane, BorderLayout.CENTER);
		
		// contact list
		JScrollPane scrollPaneContacts = new JScrollPane(getContactList());
		splitPane.add(scrollPaneContacts);
		
		// message list
		JScrollPane scrollPaneMessages = new JScrollPane(getMessageList());
		splitPane.add(scrollPaneMessages);
		

		/*
		 * Logging SOUTH
		 *  _______________
		 * |<logEntries>   |
		 * |_______________|
		 */
		// logging output under split pane, sitting on a scroll pane
		// TODO
		/*
		logPane = new JTextPane();
		logPane.setPreferredSize(new Dimension(0, 150)); // at least 150px high
		logPane.setBackground(new Color(255, 240, 220));
		logPane.setEditable(false);
		logPane.setText("Logging output..");
		
		JScrollPane scrollPaneLog = new JScrollPane(logPane);
		//frmBackupManager.getContentPane().add(scrollPaneLog, BorderLayout.SOUTH);	
		mainPanel.add(scrollPaneLog, BorderLayout.SOUTH);
		*/
		
		return mainPanel;
	}
	
	/**
	 * Builds the contact list.
	 * @return
	 */
	private JList<ContactPM> getContactList() {
		BnList contactList = new BnList();
		contactList.setModelProvider(getLocalModelProvider());
		contactList.setPath(new Path("this.contacts"));
		contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contactList.setCellRenderer(new ContactPMListCellRenderer());
		return contactList;
	}
	
	/**
	 * Builds the message list.
	 * @return
	 */
	private JList<MessagePM> getMessageList() {
		BnList messageList = new BnList();
		messageList.setModelProvider(getLocalModelProvider());
		messageList.setPath(new Path("this.currentMessages"));
		messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		messageList.setCellRenderer(new MessagePMListCellRenderer());
		return messageList;
	}
	
	/*
	 * BOILERPLATE FUN
	 */
	
	private final Link link = new Link(this);
	private ModelProvider localModelProvider;
	
	/**
	 * Returns the local {@link ModelProvider} for this class.
	 * 
	 * @return the local <code>ModelProvider</code>
	 */
	protected ModelProvider getLocalModelProvider() {
		if (localModelProvider == null) {
			localModelProvider = new ModelProvider();
			localModelProvider.setPresentationModelType(BackupManagerPM.class);
		}
		return localModelProvider;
	}
	
	@Override
	public IModelProvider getModelProvider() {
		return this.link.getModelProvider();
	}

	@Override
	public void setModelProvider(IModelProvider provider) {
		this.link.setModelProvider(provider);
	}

	@Override
	public void setPath(Path path) {
		this.link.setPath(path);
	}

	@Override
	public Path getPath() {
		return this.link.getPath();
	}

	@Override
	public BackupManagerPM getPresentationModel() {
		return getLocalModelProvider().getPresentationModel();
	}

	@Override
	public void setPresentationModel(BackupManagerPM model) {
		getLocalModelProvider().setPresentationModel(model);
	}
}
