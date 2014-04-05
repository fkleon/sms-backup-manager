package de.leonhardt.sbm.core.util;

import java.util.logging.Logger;

import de.leonhardt.sbm.core.model.AbstractEntity;

/**
 * Helper utilities and methods.
 * 
 * @author Frederik Leonhardt
 *
 */
public class Utils {

	/**
	 * An ID generator should generate unique IDs
	 * and keep track of the number of generated IDs.
	 * 
	 * @author Frederik Leonhardt
	 *
	 */
	public interface IdGenerator {
		
		/**
		 * Return the next unassigned ID.
		 * @return
		 */
		public long getNextId();
		
		/**
		 * Return the total number of generated IDs.
		 * @return
		 */
		public long getNumIds();
		
		/**
		 * Assigns the next free ID to given AbstractEntity.
		 * @param ae
		 */
		public <T extends AbstractEntity> T assignNextId(T ae);
	}
	
	/**
	 * Simple implementation of IdGenerator.
	 * Just uses integers in a row.
	 * 
	 * @author Frederik Leonhardt
	 *
	 */
	public static class SimpleIdGenerator implements IdGenerator {
		private long nextId;
		
		public SimpleIdGenerator() {
			this.nextId = 1;
		}
		
		@Override
		public long getNextId() {
			return this.nextId++;
		}

		@Override
		public long getNumIds() {
			return this.nextId;
		}

		@Override
		public <T extends AbstractEntity> T assignNextId(T ae) {
			ae.setId(getNextId());
			return ae;
		}
	}
	
	/**
	 * Returns the default ID generator.
	 * @return
	 */
	public static IdGenerator getDefaultIdGenerator() {
		return new Utils.SimpleIdGenerator();
	}
	
	/**
	 * Returns the progress in percent.
	 * 
	 * @param now iteration currently in
	 * @param total number of iterations
	 * @return percentage of completion
	 */
	public static int calcProgress(int now, int total) {
		Float progress = (((float)now/(float)total)*100.f);
		return progress.intValue();
	}
	
	/**
	 * The TimeTracker class is a helper utility to easily
	 * measure elapsed time spans for debugging.
	 * 
	 * @author Frederik Leonhardt
	 *
	 */
	public static class TimeTracker {
		
		long startTime;
		
		/**
		 * Starts the timer.
		 */
		public void start() {
			this.startTime = System.currentTimeMillis();
			Logger.getAnonymousLogger().info("Started time tracking.");
		}
		
		/**
		 * Returns the milliseconds ellapsed since timer was started.
		 * @return
		 */
		public long elapsedMillis() {
			return (System.currentTimeMillis() - this.startTime);
		}
		
		/**
		 * Returns the time elapsed since timer was started,
		 * formated as String.
		 * @return
		 */
		public String elapsedTime() {
			long ms = elapsedMillis();
			
			long s = (ms / 1000) % 60;
			//long m = (ms / (1000 * 60)) % 60;
			//long h = (ms / (1000 * 60 * 60)) % 24;
			
			return String.format("%02d seconds, %02d milliseconds", s, ms-(s*1000));
		}
		
		/**
		 * Logs the elapsed time to an anonymous logger.
		 * @param description
		 */
		public void logElapsedTime(String description) {
			Logger.getAnonymousLogger().info("["+description+"] Elapsed: " + elapsedTime());
		}
	}
	
	// logging
	public void logf(String format, Object... args) {
		Logger.getAnonymousLogger().info(String.format(format, args));
	}
	
	public void debugf(String format, Object... args) {
		Logger.getAnonymousLogger().fine(String.format(format, args));
	}
	
	public void warnf(String format, Object... args) {
		Logger.getAnonymousLogger().warning(String.format(format, args));
	}
}
