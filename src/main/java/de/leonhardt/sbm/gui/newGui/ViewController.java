package de.leonhardt.sbm.gui.newGui;

import java.awt.Component;

/**
 * A view controller offers access to important
 * GUI components, e.g. for manipulating them
 * from internal parts of the system.
 * 
 * It also offers a functionality to trigger a refresh
 * of sub-components, e.g. when a model needs to be reloaded.
 * 
 * @author Frederik Leonhardt
 *
 */
public interface ViewController {

	/**
	 * Returns the main view (outer Frame).
	 * @return
	 */
	public Component getMainView();
	
	/**
	 * Returns the Settings dialog.
	 * @return
	 */
	public Component getSettingsView();
	
	/**
	 * Something is very dirty in here..
	 */
	public void setDirty();
	
}
