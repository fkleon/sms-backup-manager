package de.leonhardt.sbm.gui.oldGui.renderer;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import de.leonhardt.sbm.core.model.Contact;
import de.leonhardt.sbm.gui.common.resource.FlagLoader;

/**
 * A renderer for ListModels containing Contacts.
 * @author Frederik Leonhardt
 *
 */
public class ContactListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 3041400497913312821L;

	// to load flag icons
	private final FlagLoader fl;

	public ContactListCellRenderer() {
		super();
		this.fl = new FlagLoader();
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
		
		// since there is no generic support in Java 6 ListModels yet, check if we actually get a contact
		if (value instanceof Contact) {
			Contact c = (Contact) value;
			Component cComp = getContactListEntryComponent(c.getContactName(), c.getCountryCode(), c.getAddressIntl());
		    //Component component = (JPanel) value;

		    /*
			// create empty border around panel for margins
			((JPanel)value).setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

			// fancy stuff
			JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
			jPanel.add(component);
			
		    //component = jPanel;
		    */
		    
		    // set colors
			cComp.setForeground (isSelected ? jlist.getSelectionForeground() : jlist.getForeground());
			cComp.setBackground (isSelected ? jlist.getSelectionBackground() : jlist.getBackground());
		    
		    return cComp;
		} else {
			// no? then return normal component
			return super.getListCellRendererComponent(jlist, value, cellIndex, isSelected, cellHasFocus);
		}
	}
	
	/**
	 *  For each contact, we build a JPanel looking somehow similar to this:
	 *  ____________________________
	 * |                            |
	 * | CONTACT NAME               |
	 * |  _______                   |
	 * | |Country|                  |
	 * | |Flag   | <Phone Number>   |
	 * | |_______|                  |
	 * |____________________________|
	 * 
	 * @param contactName
	 * @param countryCode
	 * @param contactAddress
	 */
	private Component getContactListEntryComponent(String contactName, String countryCode, String contactAddress) {
		// the main panel
		JPanel cPanel = new JPanel(new GridLayout(0, 1));
		//cPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		cPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		// we need several sub-elements
		// 1. label for the contact name
		JLabel cNameLabel = new JLabel(contactName);
		Font oldFont = cNameLabel.getFont();
		int oldFontSize = cNameLabel.getFont().getSize();
		cNameLabel.setFont(oldFont.deriveFont(oldFontSize+4f));
		
		// 2. label for flag and address
		ImageIcon flag = fl.getFlag(countryCode);
		JLabel cAddressLabel = new JLabel("<" + contactAddress+ ">", flag, JLabel.LEFT);

		// add both elements to panel
		cPanel.add(cNameLabel);
		cPanel.add(cAddressLabel);

		return cPanel;
	}

}
