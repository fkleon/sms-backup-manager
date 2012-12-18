package de.leonhardt.sbm.gui.newGui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnIconLabel;
import org.beanfabrics.swing.BnLabel;
import org.beanfabrics.swing.BnTextArea;

import de.leonhardt.sbm.gui.common.GuiUtils;
import de.leonhardt.sbm.gui.common.MessagePM;

/**
 * A View on a MessagePM.
 * @author Frederik Leonhardt
 */
@SuppressWarnings("serial")
public class MessageView extends JPanel implements View<MessagePM>, ModelSubscriber {

	private ModelProvider localModelProvider;
	private final Link link = new Link(this);
	
	/**
	 *  For each message, we build a JPanel looking somehow similar to this:
	 *  ____________________________
	 * | Date             Subject   |
	 * |                            |
	 * | Message Body               |
	 * | Message Body (ctn.)        |
	 * |____________________________|
	 * 
	 * This generates a view which has not been associated to a PM yet.
	 */
	public MessageView() {
			// the main panel
			this.setLayout(new BorderLayout(5,10));
			this.setBorder(BorderFactory.createRaisedBevelBorder());
			//mPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			
			// we need several sub-elements
			// 1. header containing icons, date and subject
			JPanel mHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

			BnIconLabel mTypeLabel = new BnIconLabel(getLocalModelProvider(), new Path("this.typeIcon"));
			BnIconLabel mStatusLabel = new BnIconLabel(getLocalModelProvider(), new Path("this.statusIcon"));
			BnLabel mDateLabel = new BnLabel(getLocalModelProvider(), new Path("this.date"));
			
			mHeaderPanel.add(mTypeLabel);
			mHeaderPanel.add(mStatusLabel);
			mHeaderPanel.add(mDateLabel);
			
			// duplicate count
			BnLabel mDupeLabel = new BnLabel(getLocalModelProvider(), new Path("this.numDuplicates"));
			Font oldFont = mDupeLabel.getFont();
			int oldFontSize = mDupeLabel.getFont().getSize();
			mDupeLabel.setFont(oldFont.deriveFont(oldFontSize-3f));
			mHeaderPanel.add(mDupeLabel);
			
			// subject
			//JLabel mSubjectLabel = new JLabel(messageSubject, RIGHT);
			
			//Font oldFont = cNameLabel.getFont();
			//int oldFontSize = cNameLabel.getFont().getSize();
			//cNameLabel.setFont(oldFont.deriveFont(oldFontSize+4f));
			
			// 2. label for body
			BnTextArea mBodyTextArea = new BnTextArea(getLocalModelProvider(), new Path("this.body"));
			mBodyTextArea = GuiUtils.makeLabelStyleTextArea(mBodyTextArea);
			
			mBodyTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			// assemble..
			this.add(mHeaderPanel, BorderLayout.NORTH);
			this.add(mBodyTextArea, BorderLayout.CENTER);
	}
	
	/**
	 * Creates the view and links it to the given presentation model.
	 * @param pm
	 */
	public MessageView(MessagePM pm) {
		this();
		setPresentationModel(pm);
	}
	
	/**
	 * Returns the local model provider.
	 * @return
	 */
	protected ModelProvider getLocalModelProvider() {
		if (localModelProvider == null) {
			localModelProvider = new ModelProvider(MessagePM.class);
		}
		
		return localModelProvider;
	}
	
	@Override
	public MessagePM getPresentationModel() {
		return this.getLocalModelProvider().getPresentationModel();
	}

	@Override
	public void setPresentationModel(MessagePM pm) {
		this.getLocalModelProvider().setPresentationModel(pm);
	}

	@Override
	public IModelProvider getModelProvider() {
		return link.getModelProvider();
	}

	@Override
	public Path getPath() {
		return link.getPath();
	}

	@Override
	public void setModelProvider(IModelProvider mp) {
		this.link.setModelProvider(mp);
	}

	@Override
	public void setPath(Path path) {
		this.link.setPath(path);
	}

}
