package de.leonhardt.sbm.gui.oldGui.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;


/**
 * A renderer for ListModels containing JPanels.
 * @author Frederik Leonhardt
 *
 */
public class PanelListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 3041400497913312821L;
	
	public PanelListCellRenderer() {
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
	public Component getListCellRendererComponent(JList<?> jlist, Object value,
			int cellIndex, boolean isSelected, boolean cellHasFocus) {
		
		// since there is no generic support in Java 6 ListModels yet, check if we actually get a message
		if (value instanceof JPanel) {
			Component cComp = (JPanel) value;
				
		    // set colors
			cComp.setForeground (isSelected ? jlist.getSelectionForeground() : jlist.getForeground());
			cComp.setBackground (isSelected ? jlist.getSelectionBackground() : jlist.getBackground());

		    return cComp;
		} else {
			// return normal component
			return super.getListCellRendererComponent(jlist, value, cellIndex, isSelected, cellHasFocus);
		}
	}
}
