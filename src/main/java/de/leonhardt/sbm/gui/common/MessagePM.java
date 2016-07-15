package de.leonhardt.sbm.gui.common;

import java.awt.Component;
import java.text.DateFormat;

import org.apache.commons.lang3.text.WordUtils;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.IconPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Service;

import de.leonhardt.sbm.core.model.Message;
import de.leonhardt.sbm.core.model.MessageConsts.Status;
import de.leonhardt.sbm.core.model.MessageConsts.Type;
import de.leonhardt.sbm.gui.common.resource.IconService;
import de.leonhardt.sbm.gui.newGui.MessageView;
import lombok.extern.log4j.Log4j2;

/**
 * Presentation Model of a message.
 * Read-only.
 *
 * @author Frederik Leonhardt
 *
 */
@Log4j2
public class MessagePM extends AbstractPM {

	// wrap messages after LINE_CHARS characters.
	public static int LINE_CHARS = 80;

	public TextPM body = new TextPM();
	public DatePM date = new DatePM();
	public TextPM address = new TextPM();
	public IconPM typeIcon = new IconPM();
	public IconPM statusIcon = new IconPM();
	public TextPM numDuplicates = new IntegerPM();

	// keep original type and status for icon lookups
	private Type messageType;
	private Status messageStatus;

	// keep view to render in list
	private MessageView view;

	// service to lookup icons
	private IconService iconService;

	public MessagePM() {
		PMManager.setup(this);

		body.setEditable(false);
		date.setEditable(false);
		date.setFormat(DateFormat.getDateTimeInstance());

		address.setEditable(false);
		numDuplicates.setEditable(false);
		numDuplicates.getValidator().clear();
	}

	public MessagePM(Message msg) {
		this();
		init(msg);
	}

	public MessagePM(Message msg, IconService iconService) {
		this();
		this.iconService = iconService;
		init(msg);
	}

	private void init(Message msg) {
		// wrap body
		String wrappedBody = WordUtils.wrap(msg.getBody(), LINE_CHARS, null, true);
		this.body.setText(wrappedBody);
		this.date.setDate(msg.getDate());
		this.address.setText(msg.getContact().getAddressIntl());

		this.messageType = msg.getType();
		this.messageStatus = msg.getStatus();

		int dupes = msg.getNumDuplicates();
		this.numDuplicates.setText(dupes == 0 ? "" : String.format("(+%d duplicate%s)", dupes, dupes>1?"s":""));

		// update icons and revalidate
		this.updateIcons();
		//this.revalidateProperties(); // no revalidation yet, since no editing is possible

		this.view = new MessageView(this);
	}

	public void setMessage(Message msg) {
		init(msg);
	}

	public Component getView() {
		if (this.view == null) {
			this.view = new MessageView(this);
		}
		return this.view;
	}

	@Service
	public void setService(IconService service) {
		this.iconService = service;

		// update icons
		this.updateIcons();
	}

	@OnChange(path= {"type", "status"})
	public void updateIcons() {
		if (iconService == null) {
			// can't do anything about that..
			log.trace("No IconService configured.");
			return;
		}
		// try to set type icon
		if (messageType != null)
			this.typeIcon.setIcon(iconService.getMessageTypeIcon(messageType));
		// try to set status icon
		if (messageStatus != null)
			this.statusIcon.setIcon(iconService.getMessageStatusIcon(messageStatus));
	}

	/**
	 * For copy & paste
	 */
	@Override
	public String toString() {
		return String.format(
				"%s: %s%n" +
				"Date: %s%n%n" +
				"%s",
				Type.Received.equals(messageType) ? "From" : "To", address.getText(),
				date.getDate(), body.getText());
	}

}
