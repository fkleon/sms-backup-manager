package de.leonhardt.sbm.gui.common.resource;

import javax.swing.ImageIcon;

import de.leonhardt.sbm.core.model.MessageConsts.Status;
import de.leonhardt.sbm.core.model.MessageConsts.Type;

/**
 * A service for flag icon lookups.
 * 
 * @author Frederik Leonhardt
 *
 */
public interface IconService {

	/**
	 * Returns the loading icon animation.
	 * @return
	 */
	public ImageIcon getLoadingAnimation();
	
	/**
	 * Returns the icon for given message type.
	 * @param mType
	 * @return
	 */
	public ImageIcon getMessageTypeIcon(Type mType);
	
	/**
	 * Returns the icon for given message status.
	 * @param mStatus
	 * @return
	 */
	public ImageIcon getMessageStatusIcon(Status mStatus);
	
	/**
	 * Returns the icon for given resource name.
	 * @param iconName
	 * @return
	 */
	public ImageIcon getIcon(String iconName);
}
