package de.leonhardt.sbm.gui.pm;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;

import de.leonhardt.sbm.xml.model.Sms;
import de.leonhardt.sbm.xml.model.Smses;

public class ConversationsPM extends AbstractPM {

	public ListPM<MessagePM> messages = new ListPM<MessagePM>();
	
	public ConversationsPM(Smses smses) {
		PMManager.setup(this);
		
		init(smses);
	}
	
	private void init(Smses smses) {
		for (Sms sms: smses.getSms()) {
			MessagePM smsPM = new MessagePM(sms);
			messages.add(smsPM);
		}
	}
	
	public void setConversations(Smses smses) {
		init(smses);
		//TODO get smses via Service?
	}
}
