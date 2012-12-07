package de.leonhardt.sbm.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;

import de.leonhardt.sbm.gui.model.Settings;
import de.leonhardt.sbm.gui.resource.FlagLoader;
import de.leonhardt.sbm.gui.resource.IconLoader;

/**
 * The Dialog Window for the settings.
 * 
 * @author Frederik Leonhardt
 *
 */
public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 3133390852790951903L;

	private static String localeHelpText = "Enter your locale consisting of 2-digit language code (ISO 639-1) and country code (ISO 3166-1), e.g. de-DE. " +
			"Your phone numbers will be interpreted based on the country code."; //TODO internationalize
	
	private IconLoader il; // need icon loader to load fancy icons
	
	private final Settings settings;
	
	private JDialog dlgSettings;
	private JTextField sLocaleTextLanguage;
	private JTextField sLocaleTextCountry;
	private JCheckBox sCheckExportInternational;
	
	public SettingsDialog(JFrame parent, boolean modal, Settings settings) {
		super(parent, "Settings" , modal); // initialize with title
		
		this.dlgSettings = this;
		this.settings = settings;
		
		this.il = new IconLoader();
		
		String[] dummy = null;
		
		/*
		 * Main panel
		 */
		JPanel sPanel = new JPanel();
		sPanel.setLayout(new BoxLayout(sPanel, BoxLayout.PAGE_AXIS));
		sPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(sPanel);

		/*
		 * Export settings
		 */
		JPanel sCheckExport = new JPanel(new GridLayout());
		sCheckExportInternational = new JCheckBox("Export numbers in international format");
		sCheckExportInternational.setSelected(settings.getExportInternationalNumbers());
		sCheckExport.add(sCheckExportInternational);
		
		/*
		 * Locale input and status fields
		 */
		// main panel for locale components
		JPanel sLocalePanel = new JPanel();
		
		// document for language (force lowercase and length 2)
		AbstractDocument languageDoc = new DefaultStyledDocument();
		languageDoc.setDocumentFilter(new LowercaseDocumentFilter(2));
		
		// document for country (force uppercase and length 2)
		AbstractDocument countryDoc = new DefaultStyledDocument();
		countryDoc.setDocumentFilter(new UppercaseDocumentFilter(2));
		
		// text fields for locale
		sLocaleTextLanguage = new JTextField(languageDoc, settings.getLanguageCode(), 2);
		sLocaleTextCountry = new JTextField(countryDoc, settings.getCountryCode(), 2);
		// flag icon for locale
		JLabel flagLabel = new JLabel();
		sLocaleTextCountry.getDocument().addDocumentListener(new CountryCodeDocumentListener(flagLabel)); // this listener changes the flag icon on each change
		sLocaleTextCountry.setText(settings.getCountryCode()); // set text to trigger document listener for first time
		
		// add all components to panel
		sLocalePanel.add(sLocaleTextLanguage);
		sLocalePanel.add(new JLabel("-"));
		sLocalePanel.add(sLocaleTextCountry);
		sLocalePanel.add(flagLabel);

		// description text
		// Note: 	LineWrap works in theory, but the JTextArea doesn't know it's preferred height
		// 			until it knows the height of the outer container. To prevent the element to be
		//			displayed too small, we have to invoke pack() twice - one time before adding the
		//			last element to the outer pane and one time after (when the JTextArea already knows
		//			its preferred height).
		JTextArea sLocaleDesc = new JTextArea();
		sLocaleDesc.setLineWrap(true);
		sLocaleDesc.setWrapStyleWord(true);
		sLocaleDesc.setText(localeHelpText);
		sLocaleDesc.setEditable(false);
		sLocaleDesc.setCursor(null);
		sLocaleDesc.setOpaque(false);
		sLocaleDesc.setFocusable(false);
		//JScrollPane sLocaleDescPane = new JScrollPane(sLocaleDesc, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		/*
		 * Buttons
		 */
		// main panel for buttons
		JPanel sButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		//sButtons.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		
		JButton sSaveButton = new JButton("Save");
		sSaveButton.addActionListener(new SaveButtonActionListener());
		
		JButton sCancelButton = new JButton("Cancel");
		sCancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false); // close on button click
			}
		});
		
		// add buttons to panel
		sButtons.add(sSaveButton);
		sButtons.add(sCancelButton);
		
		/*
		 * Putting it all together
		 */
		// build content of dialog window
		//sPanel.add(buildLabel("Settings", "settings-cogwheels"));
		//sPanel.add(new JSeparator());
		
		sPanel.add(buildHeaderLabel("Locale", "settings-locale"));
		sPanel.add(sLocaleDesc);
		sPanel.add(sLocalePanel);
		
		sPanel.add(new JSeparator());
		sPanel.add(buildHeaderLabel("Contacts", "settings-contacts"));
		sPanel.add(sCheckExport);

		/*
		sPanel.add(buildHeaderLabel("Conversations", "settings-conversations"));
		sPanel.add(new JSeparator());
		sPanel.add(buildLabel("n/a", dummy));

		sPanel.add(new JSeparator());
		sPanel.add(buildHeaderLabel("Message import", "settings-messages", "settings-import"));
		sPanel.add(new JSeparator());
		sPanel.add(buildLabel("n/a", dummy));
		
		sPanel.add(new JSeparator());
		sPanel.add(buildHeaderLabel("Message export", "settings-messages", "settings-export"));
		sPanel.add(new JSeparator());
		sPanel.add(sCheckExportInternational);
		*/
		pack();
		
		sPanel.add(new JSeparator());
		sPanel.add(sButtons);

		pack(); // repack, because of wrap line. see previous comment for explanation.
		setLocationRelativeTo(parent);
	    //setVisible(true);
	}
	
	private Component buildHeaderLabel(String text, String... iconNames) {
		JPanel comp = buildLabel(text, iconNames);
		//comp.setBorder(new EmptyBorder(10, 10, 10, 10));
		return comp;
	}
	
	private JPanel buildLabel(String text, String... iconNames) {
		JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10 ,0));
		//jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
		
		if (iconNames != null) {
			for (String iconName: iconNames) {
				JLabel jLabel = new JLabel(il.getIcon(iconName), JLabel.LEFT);
				jLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
				jPanel.add(jLabel);
			}
		}
		
		jPanel.add(new JLabel(text, JLabel.LEFT));
		return jPanel;
	}
	
	protected Settings getSettings() {
		return this.settings;
	}
	

	/**
	 * A DocumentListener, which tries to apply the appropriate CountryFlag to the given JLabel
	 * on a text change.
	 * 
	 * @author Frederik Leonhardt
	 *
	 */
	private class CountryCodeDocumentListener implements DocumentListener {

		private final JLabel flagComp;
		private final ImageIcon defaultFlag;
		private final FlagLoader fl;
		
		public CountryCodeDocumentListener(JLabel flagComponent) {
			super();
			this.flagComp = flagComponent;
			this.fl = new FlagLoader();
			this.defaultFlag = fl.getFlag("default");
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			// ignore
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			if (e.getDocument().getLength() == 2) {
				try {
					ImageIcon flag = fl.getFlag(e.getDocument().getText(0, 2));
					flagComp.setIcon(flag);
				} catch (BadLocationException e1) {
					// should never happen
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			if (e.getDocument().getLength() < 2) {
				// remove flag
				flagComp.setIcon(defaultFlag);
			}
		}
	}
	
	/**
	 * A DocumentFilter which rejects Strings longer than given LIMIT
	 * 
	 * @author Frederik Leonhardt
	 */
	public class LengthDocumentFilter extends DocumentFilter {
		private final int LIMIT;
		
		/**
		 * Creates a LengthDocumentFilter with limit = Integer.MAX_VALUE
		 */
		public LengthDocumentFilter() {
			this(Integer.MAX_VALUE);
		}
		
		/**
		 * Creates a LengthDocumentFilter with given size limitation
		 * @param lengthLimit
		 */
		public LengthDocumentFilter(int lengthLimit) {
			super();
			this.LIMIT = lengthLimit;
		}
		
		@Override
		public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			if (offset >= LIMIT || string.length() > LIMIT || offset+string.length() > LIMIT) {
				// length is not ok
				return;
			} else {
				// length is ok
				super.insertString(fb, offset, string, attr);
			}
		}
		
		@Override
		public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
//			System.out.println("[r] docLen: " +fb.getDocument().getLength());
//			System.out.println("[r] str.len: " + string.length());
//			System.out.println("[r] offset: "+ offset);
			if (offset >= LIMIT || length > LIMIT || offset+length > LIMIT) {
				// length is not ok
				return;
			} else {
				// length is ok
				super.replace(fb, offset, length, string, attr);
			}
		}
	}
	
	/**
	 * A DocumentFilter which uppercases all input.
	 * 
	 * @author Frederik Leonhardt
	 */
	public class UppercaseDocumentFilter extends LengthDocumentFilter {
	
		public UppercaseDocumentFilter(int lengthLimit) {
			super(lengthLimit);
		}
		
		@Override
		public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			super.insertString(fb, offset, string.toUpperCase(), attr);
		}
		
		@Override
		public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
			super.replace(fb, offset, length, string.toUpperCase(), attr);
		}
	}
	
	/**
	 * A DocumentFilter which lowercases all input.
	 * 
	 * @author Frederik Leonhardt
	 */
	public class LowercaseDocumentFilter extends LengthDocumentFilter {
		
		public LowercaseDocumentFilter(int lengthLimit) {
			super(lengthLimit);
		}

		@Override
		public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			super.insertString(fb, offset, string.toLowerCase(), attr);
		}
		
		@Override
		public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
			super.replace(fb, offset, length, string.toLowerCase(), attr);
		}
	}
	
	public class SaveButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				settings.setLanguageCode(sLocaleTextLanguage.getText());
				settings.setCountryCode(sLocaleTextCountry.getText());
				settings.setExportInternationalNumbers(sCheckExportInternational.isSelected());
				settings.save();
				dlgSettings.setVisible(false);
			} catch (IllegalArgumentException ex) {
				//oopsies
				GuiUtils.alertError(dlgSettings, "Error saving settings", "Invalid settings: " + ex.toString());
				settings.load();
			}
		}
		
	}
	
}
