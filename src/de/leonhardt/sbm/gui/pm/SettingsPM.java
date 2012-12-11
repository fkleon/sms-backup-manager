package de.leonhardt.sbm.gui.pm;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Service;
import org.beanfabrics.support.Validation;

import de.leonhardt.sbm.gui.model.Settings;
import de.leonhardt.sbm.gui.service.SettingsService;

/**
 * The Presentation Model of the Settings Dialog.
 * 
 * @author Frederik Leonhardt
 *
 */
public class SettingsPM extends AbstractPM {

	public TextPM languageCode = new TextPM();
	public CountryFlagPM countryFlag = new CountryFlagPM();
	public BooleanPM exportIntl = new BooleanPM();
	public BooleanPM exportDupes = new BooleanPM();
	public OperationPM save = new OperationPM();
	public OperationPM cancel = new OperationPM();
	
	private SettingsService controller;
	
	public SettingsPM() {
		PMManager.setup(this);
		
		this.languageCode.setEditable(true);
		this.languageCode.setMandatory(true);

		this.exportIntl.setEditable(true);
		this.exportDupes.setEditable(true);
	}
	
	public SettingsPM(String languageCode, String countryCode, boolean exportIntl, boolean exportDupes) {
		this();
		initModels(languageCode, countryCode, exportIntl, exportDupes);
	}
	
	private void initModels(String languageCode, String countryCode, boolean exportIntl, boolean exportDupes) {
		this.languageCode.setText(languageCode);
		this.countryFlag.setCountryCode(countryCode);
		this.exportIntl.setBoolean(exportIntl);
		this.exportDupes.setBoolean(exportDupes);
	}
	
	public void setSettings(Settings settings) {
		initModels(settings.getLanguageCode(), settings.getCountryCode(), settings.getExportInternationalNumbers(), settings.isExportDupes());
	}

	@Service
	public void setController(SettingsService controller) {
		this.controller = controller;
		
		// load data and revalidate
		this.reset();
		this.revalidateProperties();
	}
	
	@OnChange(path="languageCode")
	public void updateLanguage() {
		String lText = cutToLength(languageCode.getText().toLowerCase(),2);
		this.languageCode.setText(lText);
	}
	
	@Operation(path="save")
	public void save() {
		controller.store(countryFlag.countryCode.getText(), languageCode.getText(), exportIntl.getBoolean(), exportDupes.getBoolean());
	}
	
	@Validation(path="languageCode")
	public boolean validateLanguageCode() {
		String lCode = languageCode.getText();
		return (lCode != null && !lCode.isEmpty() && lCode.length()==2);
	}
	
	@Validation(path="save")
	public boolean canSave() {
		return (countryFlag.isValid() && this.isValid() && controller != null);
	}
	
	@Operation(path="cancel")
	public void reset() {
		// reset settings
		if(controller != null) {
			setSettings(controller.getSettings());
		}
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
