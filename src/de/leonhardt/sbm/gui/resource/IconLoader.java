package de.leonhardt.sbm.gui.resource;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

//TODO refactor, rewrite
public class IconLoader extends ResourceLoader {

	private static String resPath = "/resources/images/uiIcons/%s.png";

	private static Map<Integer, String> mTypeIconMap = new HashMap<Integer, String>();
	static {
		// 1 = Received, 2 = Sent, 3 = Draft, 4 = Outbox, 5 = Failed, 6 = Queued
		mTypeIconMap.put(1, "message-in");
		mTypeIconMap.put(2, "message-out");
		mTypeIconMap.put(3, "message-draft");
		mTypeIconMap.put(4, "message-outbox");
		mTypeIconMap.put(5, "message-fail");
		mTypeIconMap.put(6, "message-queue");
	}
	
	private static Map<Integer, String> mStatusIconMap = new HashMap<Integer, String>();
	static {
		// None = -1, Complete = 0, Pending = 32, Failed = 64.
		//mTypeIconMap.put(-1, "status-none");
		mTypeIconMap.put(0, "status-ok");
		//mTypeIconMap.put(32, "status-pending");
		mTypeIconMap.put(64, "status-fail");
	}
	
	public ImageIcon getMessageTypeIcon(Integer mType) {
		if (mTypeIconMap.containsKey(mType)) {
			return getIcon2(mTypeIconMap.get(mType));
		} else {
			return new ImageIcon();
		}
	}
	
	public ImageIcon getMessageStatusIcon(Integer mStatus) {
		if (mStatusIconMap.containsKey(mStatus)) {
			return getIcon2(mStatusIconMap.get(mStatus));
		} else {
			return new ImageIcon();
		}
	}
	
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
			Logger.getAnonymousLogger().info("Could not find icon '"+normalizedIconName+"' (URL null)");
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
