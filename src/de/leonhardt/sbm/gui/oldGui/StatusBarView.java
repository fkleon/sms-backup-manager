package de.leonhardt.sbm.gui.oldGui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import org.beanfabrics.ModelProvider;
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
public class StatusBarView extends JPanel implements View<StatusBarPM> {

	private static final long serialVersionUID = 1117959294003972902L;

	private ModelProvider localModelProvider;
	
	/**
	 * A status bar contains:
	 *  _______________________________________________
	 * |[Status Icon] [Text]             [ProgressBar] |
	 * |_______________________________________________|
	 */
	public StatusBarView() {
		getLocalModelProvider(); // init
		
		BnIconLabel iconLabel = new BnIconLabel(localModelProvider, new Path("this.icon"));
		BnProgressBar progressBar = new BnProgressBar(localModelProvider, new Path("this.progress"));
		BnLabel textLabel = new BnLabel(localModelProvider, new Path("this.status"));
		init(iconLabel, progressBar, textLabel);
	}
	
	private void init(JLabel iconLabel, JProgressBar progressBar, JLabel textLabel) {
		//this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setLayout(new BorderLayout());
		JPanel iconTextPanel = new JPanel();
		iconTextPanel.add(iconLabel);
		iconTextPanel.add(textLabel);
		
		this.add(iconTextPanel, BorderLayout.WEST);
		this.add(progressBar, BorderLayout.EAST);
		
		this.setBorder(new LineBorder(Color.BLACK));
	}
	
	protected ModelProvider getLocalModelProvider() {
		if (localModelProvider == null) {
			localModelProvider = new ModelProvider();
			localModelProvider.setPresentationModelType(StatusBarPM.class);
		}
		return localModelProvider;
	}

	@Override
	public StatusBarPM getPresentationModel() {
		return getLocalModelProvider().getPresentationModel();
	}

	@Override
	public void setPresentationModel(StatusBarPM arg0) {
		getLocalModelProvider().setPresentationModel(arg0);
	}
}
