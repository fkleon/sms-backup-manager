package de.leonhardt.sbm.gui.newGui.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import de.leonhardt.sbm.gui.common.MessagePM;
/**
 * A renderer for ListModels containing MessagePMs.
 * 
 * Accesses the pre-generated view in the MessagePM to save performance.
 * 
 * @author Frederik Leonhardt
 *
 */
public class MessagePMListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 3041400497913312821L;
	
	/**
	 * Creates a new MessagePMCellRenderer
	 */
	public MessagePMListCellRenderer() {
		super();
	}
	
	/**
	   * From http://java.sun.com/javase/6/docs/api/javax/swing/ListCellRenderer.html:
	   * 
	   * Return a component that has been configured to display the specified value. 
	   * That component's paint method is then called to "render" the cell. 
	   * If it is necessary to compute the dimensions of a list because the list cells do not have a fixed size, 
	   * this method is called to generate a component on which getPreferredSize can be invoked. 
	   * 
	   * jlist - the jlist we're painting
	   * value - the value returned by list.getModel().getElementAt(index).
	   * cellIndex - the cell index
	   * isSelected - true if the specified cell is currently selected
	   * cellHasFocus - true if the cell has focus
	   */
	@Override
	public Component getListCellRendererComponent(JList jlist, Object value,
			int cellIndex, boolean isSelected, boolean cellHasFocus) {
		
		// since there is no generic support in Java 6 ListModels yet, check if we actually get a message
		if (value instanceof MessagePM) {
			MessagePM message = (MessagePM) value;
			
			// get the view
			Component cComp = message.getView();
			
		    // and set colours
			cComp.setForeground (isSelected ? jlist.getSelectionForeground() : jlist.getForeground());
			cComp.setBackground (isSelected ? jlist.getSelectionBackground() : jlist.getBackground());

		    return cComp;
		} else {
			// not supported, return normal component
			return super.getListCellRendererComponent(jlist, value, cellIndex, isSelected, cellHasFocus);
		}
	}

}
