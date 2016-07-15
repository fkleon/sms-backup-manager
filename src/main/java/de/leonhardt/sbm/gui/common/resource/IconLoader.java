package de.leonhardt.sbm.gui.common.resource;

import java.net.URL;

import javax.swing.ImageIcon;

import de.leonhardt.sbm.core.model.MessageConsts.Status;
import de.leonhardt.sbm.core.model.MessageConsts.Type;
import lombok.extern.log4j.Log4j2;

//TODO refactor, rewrite
@Log4j2
public class IconLoader extends ResourceLoader implements IconService {

	private static String resPath = "/images/uiIcons/%s";


	@Override
	public ImageIcon getLoadingAnimation() {
		return getIcon2("load-anim.gif");
	}

	@Override
	public ImageIcon getMessageTypeIcon(Type mType) {
		if (mType == null) throw new IllegalArgumentException("Type cannot be null");
		return getIcon2(mType.getIcon());
	}

	@Override
	public ImageIcon getMessageStatusIcon(Status mStatus) {
		if (mStatus == null) throw new IllegalArgumentException("Status cannot be null");
		return getIcon2(mStatus.getIcon());
	}

	@Override
	public ImageIcon getIcon(String iconName) {
		if (iconName != null && iconName.length() > 0) {
			String nIconName = normalize(iconName);
			return getIcon2(nIconName);
		} else {
			return new ImageIcon();
		}
	}

	private ImageIcon getIcon2(String normalizedIconName) {
		URL iconURL = getResourceURL(buildResPath(normalizedIconName));
		if (iconURL == null) {
			log.debug("Could not find icon '"+normalizedIconName+"' (URL null)");
			return new ImageIcon();
		} else {
			return new ImageIcon(iconURL, normalizedIconName);
		}
	}

	/**
	 * Formats the resource path.
	 *
	 * @param iconName
	 * @return
	 */
	private String buildResPath(String iconName) {
		return String.format(resPath, iconName);
	}

}
