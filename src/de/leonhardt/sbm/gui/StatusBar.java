package de.leonhardt.sbm.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;

public class StatusBar extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 1117959294003972902L;

	private JLabel iconLabel;
	private JProgressBar progressBar;
	private JLabel textLabel;
	
	/**
	 * A status bar contains:
	 *  _______________________________________________
	 * |[Status Icon] [Text]             [ProgressBar] |
	 * |_______________________________________________|
	 */
	public StatusBar() {
		JLabel iconLabel = new JLabel();
		JProgressBar progressBar = new JProgressBar();
		progressBar.setVisible(false);
		JLabel textLabel = new JLabel("BackupManager..");
		init(iconLabel, progressBar, textLabel);
	}
	
	public StatusBar(JLabel iconLabel, JProgressBar progressBar, JLabel textLabel) {
		init(iconLabel, progressBar, textLabel);
	}
	
	public StatusBar(ImageIcon iconLabel, JProgressBar progressBar, String text) {
		this(new JLabel(iconLabel), progressBar, new JLabel(text));
	}
	
	private void init(JLabel iconLabel, JProgressBar progressBar, JLabel textLabel) {
		this.iconLabel = iconLabel;
		this.progressBar = progressBar;
		this.textLabel = textLabel;
		
		//this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setLayout(new BorderLayout());
		JPanel iconTextPanel = new JPanel();
		iconTextPanel.add(iconLabel);
		iconTextPanel.add(textLabel);
		
		this.add(iconTextPanel, BorderLayout.WEST);
		//this.add(progressBar, BorderLayout.CENTER);
		this.add(progressBar, BorderLayout.EAST);
		
		this.setBorder(new LineBorder(Color.BLACK));
	}
	
	public JLabel getIconLabel() {
		return iconLabel;
	}
	
	public JLabel getTextLabel() {
		return textLabel;
	}
	
	public void setText(String text) {
		this.textLabel.setText(text);
	}
	
	public void setProgress(int progress) {
		this.progressBar.setVisible(true);
		this.progressBar.setValue(progress);
	}
	
	public void setIcon(ImageIcon icon) {
		this.iconLabel.setIcon(icon);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// automatically capture progress information
		if ("progress".equals(evt.getPropertyName())) {
			progressBar.setIndeterminate(false);
			this.setProgress((Integer)evt.getNewValue());
		}
		
		// automatically capture state information
		if ("state".equals(evt.getPropertyName())
                && SwingWorker.StateValue.DONE == evt.getNewValue()) {
            this.setIcon(null);
            this.progressBar.setVisible(false);
            this.setText("Done!");
        }
	}
}
