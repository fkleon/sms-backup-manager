package de.leonhardt.sbm.gui.newGui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnIconLabel;
import org.beanfabrics.swing.BnLabel;
import org.beanfabrics.swing.BnProgressBar;

import de.leonhardt.sbm.gui.common.StatusBarPM;

/**
 * A View on a StatusBarPM.
 * 
 * @author Frederik Leonhardt
 *
 */
public class BackupManagerStatusBar extends JPanel implements View<StatusBarPM>, ModelSubscriber {

	private static final long serialVersionUID = 1117959294003972902L;
	
	/**
	 * A status bar contains:
	 *  _______________________________________________
	 * |[Status Icon] [Text]             [ProgressBar] |
	 * |_______________________________________________|
	 */
	public BackupManagerStatusBar() {
		getLocalModelProvider(); // init
		
		BnIconLabel iconLabel = new BnIconLabel(localModelProvider, new Path("this.icon"));
		BnProgressBar progressBar = new BnProgressBar(localModelProvider, new Path("this.progress"));
		BnLabel textLabel = new BnLabel(localModelProvider, new Path("this.status"));
		init(iconLabel, progressBar, textLabel);
	}
	
	private void init(JLabel iconLabel, JProgressBar progressBar, JLabel textLabel) {
		this.setLayout(new BorderLayout());
		JPanel iconTextPanel = new JPanel();
		iconTextPanel.add(iconLabel);
		iconTextPanel.add(textLabel);
		
		this.add(iconTextPanel, BorderLayout.WEST);
		this.add(progressBar, BorderLayout.EAST);
		
		this.setBorder(new LineBorder(Color.BLACK));
	}
	
	/*
	 * View and ModelSubscriber methods
	 */
	
	private final Link link = new Link(this);
	private ModelProvider localModelProvider;
	
	/**
	 * Returns the local {@link ModelProvider} for this class.
	 * 
	 * @return the local <code>ModelProvider</code>
	 */
	protected ModelProvider getLocalModelProvider() {
		if (localModelProvider == null) {
			localModelProvider = new ModelProvider();
			localModelProvider.setPresentationModelType(StatusBarPM.class);
		}
		return localModelProvider;
	}
	
	@Override
	public IModelProvider getModelProvider() {
		return this.link.getModelProvider();
	}

	@Override
	public void setModelProvider(IModelProvider provider) {
		this.link.setModelProvider(provider);
	}

	@Override
	public void setPath(Path path) {
		this.link.setPath(path);
	}

	@Override
	public Path getPath() {
		return this.link.getPath();
	}

	@Override
	public StatusBarPM getPresentationModel() {
		return getLocalModelProvider().getPresentationModel();
	}

	@Override
	public void setPresentationModel(StatusBarPM model) {
		getLocalModelProvider().setPresentationModel(model);
	}
}
