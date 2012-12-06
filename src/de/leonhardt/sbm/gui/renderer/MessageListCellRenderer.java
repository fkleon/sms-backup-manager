package de.leonhardt.sbm.gui.renderer;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import de.leonhardt.sbm.gui.resource.FlagLoader;
import de.leonhardt.sbm.gui.resource.IconLoader;
import de.leonhardt.sbm.xml.model.Contact;
import de.leonhardt.sbm.xml.model.Sms;

public class MessageListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 3041400497913312821L;
	
	private final IconLoader il;

	public MessageListCellRenderer() {
		super();
		this.il = new IconLoader();
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
		
		if (value instanceof Sms) {
			Sms sms = (Sms) value;
			
			Date smsDate = new Date(sms.getDate());
			Component cComp = getMessageListEntryComponent(sms.getType(), smsDate, sms.getSubject(), sms.getBody(), sms.getStatus());
	
		    // set colors
			cComp.setForeground (isSelected ? jlist.getSelectionForeground() : jlist.getForeground());
			cComp.setBackground (isSelected ? jlist.getSelectionBackground() : jlist.getBackground());
		    
		    return cComp;
		} else {
			// return normal component
			return super.getListCellRendererComponent(jlist, value, cellIndex, isSelected, cellHasFocus);
		}
	}
	
	/**
	 *  For each message, we build a JPanel looking somehow similar to this:
	 *  ____________________________
	 * | Date             Subject   |
	 * |                            |
	 * | Message Body               |
	 * | Message Body (ctn.)        |
	 * |____________________________|
	 * 
	 * @param contactName
	 * @param countryCode
	 * @param contactAddress
	 */
	private Component getMessageListEntryComponent(int messageType, Date messageDate, String messageSubject, String messageBody, int messageStatus) {
		// the main panel
		JPanel mPanel = new JPanel(new GridLayout(0, 1));
		
		//cPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		mPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		// we need several sub-elements
		ImageIcon mTypeIcon = il.getMessageTypeIcon(messageType);
		ImageIcon mStatusIcon = il.getMessageStatusIcon(messageStatus);
		
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		JLabel mDateLabel = new JLabel(df.format(messageDate), mTypeIcon, LEFT);
		JLabel mStatusLabel = new JLabel(mStatusIcon);
		
		JPanel mHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		mHeaderPanel.add(mDateLabel);
		mHeaderPanel.add(mStatusLabel);
		
		// 2. subject
		//JLabel mSubjectLabel = new JLabel(messageSubject, RIGHT);
		
		//Font oldFont = cNameLabel.getFont();
		//int oldFontSize = cNameLabel.getFont().getSize();
		//cNameLabel.setFont(oldFont.deriveFont(oldFontSize+4f));
		
		// 3. label for body
		//ImageIcon flag = fl.getFlag(countryCode);
		//JLabel cAddressLabel = new JLabel("<" + contactAddress+ ">", flag, JLabel.LEFT);
		JTextArea mBodyTextArea = new JTextArea(0,1);
		mBodyTextArea.setEditable(false);
		mBodyTextArea.setCursor(null);
		mBodyTextArea.setOpaque(false);
		mBodyTextArea.setFocusable(false);
		mBodyTextArea.setLineWrap(true); //TODO wrapping buggy here, because preferred size does not adjust
		mBodyTextArea.setWrapStyleWord(true);
		mBodyTextArea.setText(messageBody);
		//System.out.println(messageBody);
		System.out.println("heigth:"+ mBodyTextArea.getPreferredSize().getHeight());
		//JLabel mBodyLabel = new JLabel(messageBody, LEFT);
		
		
		mPanel.add(mHeaderPanel);
		//mPanel.add(mSubjectLabel);
		mPanel.add(mBodyTextArea);

		return mPanel;
	}

}
