package de.leonhardt.sbm.gui.newGui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnMenuItem;

/**
 * The menu bar for the BackupManagerGUI.
 * It is one view of the BackupManagerPM.
 * @author Frederik Leonhardt
 *
 */
public class BackupManagerMenuBar extends JMenuBar implements View<BackupManagerPM>, ModelSubscriber {

	private static final long serialVersionUID = -952633721516815315L;
	
	/**
	 * Creates a new menu bat
	 */
	public BackupManagerMenuBar() {
		super();
		
		// add message menu
		this.add(getMessageMenu());
		
		// add help menu
		this.add(getHelpMenu());
	}
	
	/**
	 * Builds the message menu
	 * @return
	 */
	private JMenu getMessageMenu() {
		JMenu mnMessages = new JMenu("Messages");
		mnMessages.add(getImportEntry());
		mnMessages.add(getExportAllEntry());
		mnMessages.add(getExportSelectedEntry());
		mnMessages.add(new JSeparator());
		mnMessages.add(getSettingsEntry());
		
		return mnMessages;
	}
	
	private BnMenuItem getImportEntry() {
		BnMenuItem menuItem = new BnMenuItem(getLocalModelProvider(), new Path("this.importMessages"));
		menuItem.setText("Import");
		return menuItem;
	}
	
	private BnMenuItem getExportSelectedEntry() {
		BnMenuItem menuItem = new BnMenuItem(getLocalModelProvider(), new Path("this.exportSelectedMessages"));
		menuItem.setText("Export selected");
		return menuItem;
	}
	
	private BnMenuItem getExportAllEntry() {
		BnMenuItem menuItem = new BnMenuItem(getLocalModelProvider(), new Path("this.exportAllMessages"));
		menuItem.setText("Export all");
		return menuItem;
	}
	
	private BnMenuItem getSettingsEntry() {
		BnMenuItem menuItem = new BnMenuItem(getLocalModelProvider(), new Path("this.openSettings"));
		menuItem.setText("Settings");
		return menuItem;
	}
	
	/**
	 * Builds the help menu
	 */
	@Override //TODO this overrides a useless method, maybe rename
	public JMenu getHelpMenu() {
		JMenu mnHelp = new JMenu("Help");		
		mnHelp.add(getLogEntry());
		mnHelp.add(new JSeparator());
		mnHelp.add(getAboutEntry());
		return mnHelp;
	}
	
	private BnMenuItem getAboutEntry() {
		BnMenuItem mntmAbout = new BnMenuItem(getLocalModelProvider(), new Path("this.openAbout"));
		mntmAbout.setText("About");
		return mntmAbout;
	}

	
	private BnMenuItem getLogEntry() {
		BnMenuItem mntmAbout = new BnMenuItem(getLocalModelProvider(), new Path("this.openLog"));
		mntmAbout.setText("View Log");
		return mntmAbout;
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
