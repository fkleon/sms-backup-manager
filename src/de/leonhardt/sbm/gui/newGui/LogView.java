package de.leonhardt.sbm.gui.newGui;

import javax.swing.JDialog;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;

@SuppressWarnings("serial")
public class LogView extends JDialog implements View<LogPM>, ModelSubscriber {

	@Override
	public IModelProvider getModelProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setModelProvider(IModelProvider arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPath(Path arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public LogPM getPresentationModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPresentationModel(LogPM arg0) {
		// TODO Auto-generated method stub
		
	}

}
