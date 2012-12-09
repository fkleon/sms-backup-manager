package de.leonhardt.sbm.gui.pm;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.IconPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Service;
import org.beanfabrics.support.Validation;

import de.leonhardt.sbm.gui.model.Settings;
import de.leonhardt.sbm.gui.resource.FlagLoader;

/**
 * The Presentation Model of the Settings Dialog
 * 
 * @author Frederik Leonhardt
 *
 */
public class SettingsPM extends AbstractPM {

	public TextPM languageCode = new TextPM();
	public TextPM countryCode = new TextPM();
	public IconPM countryFlag = new IconPM();
	public BooleanPM exportIntl = new BooleanPM();
	public OperationPM save = new OperationPM();
	public OperationPM cancel = new OperationPM();
	
	private final FlagLoader fl;
	private SettingsService controller;
	
	public SettingsPM() {
		PMManager.setup(this);
		
		this.fl = new FlagLoader();

		this.languageCode.setEditable(true);
		this.languageCode.setMandatory(true);
		
		this.countryCode.setEditable(true);
		this.countryCode.setMandatory(true);

		this.exportIntl.setEditable(true);
	}
	
	public SettingsPM(String languageCode, String countryCode, boolean exportIntl) {
		this();
		initModels(languageCode, countryCode, exportIntl);
	}
	
	private void initModels(String languageCode, String countryCode, boolean exportIntl) {
		this.languageCode.setText(languageCode);
		this.countryCode.setText(countryCode);
		this.exportIntl.setBoolean(exportIntl);
		
		// load flag
		updateFlag();
	}
	
	public void setSettings(Settings settings) {
		initModels(settings.getLanguageCode(), settings.getCountryCode(), settings.getExportInternationalNumbers());
	}

	@Service
	public void setController(SettingsService controller) {
		this.controller = controller;
		this.revalidateProperties();
	}
	
	@OnChange(path="countryCode")
	public void updateFlag() {
		String cText = cutToLength(countryCode.getText().toUpperCase(),2);
		this.countryCode.setText(cText);
		this.countryFlag.setIcon(this.fl.getFlag(countryCode.getText()));
	}
	
	@OnChange(path="languageCode")
	public void updateLanguage() {
		String lText = cutToLength(languageCode.getText().toLowerCase(),2);
		this.languageCode.setText(lText);
	}
	
	@Operation(path="save")
	public void save() {
		controller.store(countryCode.getText(), languageCode.getText(), exportIntl.getBoolean());
	}
	
	@Validation(path="save")
	public boolean canSave() {
		String lCode = languageCode.getText();
		String cCode = countryCode.getText();
		// user can save, if countryCode and languageCode seem to be ok and controller is available
		return (!lCode.isEmpty() && lCode.length()==2 && !cCode.isEmpty() && lCode.length()==2 && controller != null);
	}
	
	@Operation
	public void cancel() {
		setSettings(controller.getSettings());
	}
	
	/**
	 * Cuts the given string to a given length.
	 * @param text
	 * @param length
	 * @return
	 */
	private String cutToLength(String text, int length) {
		if (text != null && !text.isEmpty() && text.length() > 2) {
			return text.substring(0,2);
		} else {
			return text;
		}
	}
}
