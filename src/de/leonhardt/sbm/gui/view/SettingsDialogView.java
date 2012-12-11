package de.leonhardt.sbm.gui.view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnCheckBox;
import org.beanfabrics.swing.BnIconLabel;
import org.beanfabrics.swing.BnTextField;

import de.leonhardt.sbm.gui.GuiUtils;
import de.leonhardt.sbm.gui.pm.SettingsPM;
import de.leonhardt.sbm.gui.resource.IconLoader;

/**
 * The SettingsDialogView is a {@link View} on a {@link de.leonhardt.sbm.gui.pm.SettingsPM}.
 *
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org 
 */
@SuppressWarnings("serial")
public class SettingsDialogView extends JDialog implements View<SettingsPM> {
	private ModelProvider localModelProvider;

	private static String localeHelpText = "Enter your locale consisting of 2-digit language code (ISO 639-1) and country code (ISO 3166-1), e.g. de-DE. " +
			"Your phone numbers will be interpreted based on the country code."; //TODO internationalize

	private final IconLoader il;
	
	/**
	 * Constructs a new <code>SettingsDialog</code>.
	 */
	public SettingsDialogView(JFrame parent) {
		super(parent, "Settings", true);
		getLocalModelProvider(); // init
		
		this.il = new IconLoader();
		
		/*
		 * Main panel
		 */
		JPanel sPanel = new JPanel();
		sPanel.setLayout(new BoxLayout(sPanel, BoxLayout.PAGE_AXIS));
		sPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(sPanel);
		

		/*
		 * Contact Export settings
		 */
		JPanel sCheckExport = new JPanel(new GridLayout(0,1));
		BnCheckBox sExportIntl = new BnCheckBox(localModelProvider, new Path("this.exportIntl"));
		sExportIntl.setText("Export numbers in international format");
		sCheckExport.add(sExportIntl);

		/*
		 * Message Export settings
		 */
		JPanel sCheckDupes = new JPanel(new GridLayout(0,1));
		BnCheckBox sExportDupes = new BnCheckBox(localModelProvider, new Path("this.exportDupes"));
		sExportDupes.setText("Export duplicate messages");
		sCheckDupes.add(sExportDupes);
		
		/*
		 * Locale input and status fields
		 */
		// main panel for locale components
		JPanel sLocalePanel = new JPanel();
		
		// text fields for locale
		BnTextField sLocaleTextLanguage = new BnTextField(localModelProvider, new Path("this.languageCode"));
		BnTextField sLocaleTextCountry = new BnTextField(localModelProvider, new Path("this.countryFlag.countryCode"));
		// flag icon for locale
		BnIconLabel sFlagLabel = new BnIconLabel(localModelProvider, new Path("this.countryFlag.countryFlagIcon"));
		
		sLocalePanel.add(sLocaleTextLanguage);
		sLocalePanel.add(new JLabel("-"));
		sLocalePanel.add(sLocaleTextCountry);
		sLocalePanel.add(sFlagLabel);
		
		// description text
		// Note: 	LineWrap works in theory, but the JTextArea doesn't know it's preferred height
		// 			until it knows the height of the outer container. To prevent the element to be
		//			displayed too small, we have to invoke pack() twice - one time before adding the
		//			last element to the outer pane and one time after (when the JTextArea already knows
		//			its preferred height).
		JTextArea sLocaleDesc = GuiUtils.buildLabelStyleTextArea(localeHelpText);
		sLocaleDesc.setLineWrap(true);
		sLocaleDesc.setWrapStyleWord(true);

		
		/*
		 * Buttons
		 */
		// main panel for buttons
		JPanel sButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		DialogCloseListener dcl = new DialogCloseListener();
		BnButton sSaveButton = new BnButton(localModelProvider, new Path("this.save"));
		sSaveButton.setText("Save");
		sSaveButton.addActionListener(dcl);
		BnButton sCancelButton = new BnButton(localModelProvider, new Path("this.cancel"));
		sCancelButton.setText("Cancel");
		sCancelButton.addActionListener(dcl);
		
		// add buttons to panel
		sButtons.add(sSaveButton);
		sButtons.add(sCancelButton);
		
		/*
		 * Putting it all together
		 */
		// build content of dialog window
		sPanel.add(buildHeaderLabel("Locale", "settings-locale.png"));
		sPanel.add(sLocaleDesc);
		sPanel.add(sLocalePanel);
		
		sPanel.add(new JSeparator());
		sPanel.add(buildHeaderLabel("Contacts", "settings-contacts.png"));
		sPanel.add(sCheckExport);
		
		sPanel.add(new JSeparator());
		sPanel.add(buildHeaderLabel("Messages", "settings-messages.png"));
		sPanel.add(sCheckDupes);
		
		pack(); // two pack()'s, explained above
		sPanel.add(new JSeparator());
		sPanel.add(sButtons);
		pack();
	}
	
	private Component buildHeaderLabel(String text, String... iconNames) {
		JPanel comp = buildLabel(text, iconNames);
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
	
	/**
	 * Returns the local {@link ModelProvider} for this class.
	 * @return the local <code>ModelProvider</code>
	 * @wbp.nonvisual location=10,430
	 */
	protected ModelProvider getLocalModelProvider() {
		if (localModelProvider == null) {
			localModelProvider = new ModelProvider(); // @wb:location=10,430
			localModelProvider.setPresentationModelType(SettingsPM.class);
		}
		return localModelProvider;
	}
	
	/** {@inheritDoc} */
	public SettingsPM getPresentationModel() {
		return getLocalModelProvider().getPresentationModel();
	}

	/** {@inheritDoc} */
	public void setPresentationModel(SettingsPM pModel) {
		getLocalModelProvider().setPresentationModel(pModel);
	}
	
	/**
	 * Listener which just hides the dialog on button action
	 * @author Frederik Leonhardt
	 */
	private class DialogCloseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
		
	}
}