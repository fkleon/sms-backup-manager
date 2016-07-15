package de.leonhardt.sbm.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.log4j.Log4j2;

/**
 * Provides information about the build of the app at runtime.
 * Reads from a properties file which is populated by the maven resources plugin.
 */
@Log4j2
public class AppInfo {

	/** The artifact version */
	public final static String VERSION;

	/** Formatted date denoting the build time of the app */
	public final static String BUILD_TIME;

	static {
		final Properties props = new Properties();

		try (InputStream propInputStream = AppInfo.class.getResourceAsStream("/app.properties")) {
			props.load(propInputStream);
		} catch (IOException e) {
			log.error("Failed to load app.properties: {}", e.getMessage(), e);
		}

		VERSION = props.getProperty("app.version");
		BUILD_TIME = props.getProperty("app.buildtime");
	}

}
