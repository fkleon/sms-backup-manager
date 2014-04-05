package de.leonhardt.sbm.gui.newGui;

import de.leonhardt.sbm.core.exception.MessageIOException;
import de.leonhardt.sbm.core.service.ContactService;
import de.leonhardt.sbm.core.service.MessageConverterService;
import de.leonhardt.sbm.core.service.MessageIOService;
import de.leonhardt.sbm.core.service.MessageService;

/**
 * The main BackupManagerService.
 * Contains references to:
 * - MessageIOService to read and write files
 * - MessageService to work with messages
 * - ContactServices to work with contacts
 * - MesageConverterService to convert messages
 * 
 * @author Frederik Leonhardt
 *
 */
public class BackupManagerService<T> {

	private MessageIOService<T> messageIO;
	private MessageService messageService;
	private ContactService contactService;
	private MessageConverterService<T> converterService;
	
	public BackupManagerService(MessageIOService<T> mio, MessageService ms, ContactService cs, MessageConverterService<T> cons) throws MessageIOException {
		this.messageIO = mio;
		this.messageService = ms;
		this.contactService = cs;
		this.setConverterService(cons);
	}

	public MessageIOService<T> getMessageIO() {
		return messageIO;
	}

	public void setMessageIO(MessageIOService<T> messageIO) {
		this.messageIO = messageIO;
	}
	
	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public ContactService getContactService() {
		return contactService;
	}

	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}

	public MessageConverterService<T> getConverterService() {
		return converterService;
	}

	public void setConverterService(MessageConverterService<T> converterService) {
		this.converterService = converterService;
	}

	
}
