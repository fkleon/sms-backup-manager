package de.leonhardt.sbm.util;

import java.util.Collections;
import java.util.logging.Logger;

import de.leonhardt.sbm.util.comparator.MessageDateComparator;
import de.leonhardt.sbm.xml.model.Smses;

//TODO comments
public class Utils {

	public interface IdGenerator {
		public long getNextId();
		public long getNumIds();
	}
	
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
	}
	
	public static IdGenerator getDefaultIdGenerator() {
		return new Utils.SimpleIdGenerator();
	}
	
	public static void sortSmses(Smses smses) {
		Collections.sort(smses.getSms(), new MessageDateComparator());
	}
	
	public static class TimeTracker {
		
		long startTime;
		
		public void start() {
			this.startTime = System.currentTimeMillis();
			Logger.getAnonymousLogger().info("Started time tracking.");
		}
		
		public long ellapsedMillis() {
			return (System.currentTimeMillis() - this.startTime);
		}
		
		public String ellapsedTime() {
			long ms = ellapsedMillis();
			
			long s = (ms / 1000) % 60;
			//long m = (ms / (1000 * 60)) % 60;
			//long h = (ms / (1000 * 60 * 60)) % 24;
			
			return String.format("%02d seconds, %02d milliseconds", s, ms);
		}
		
		public void logEllapsedTime(String description) {
			Logger.getAnonymousLogger().info("["+description+"] Ellapsed: " + ellapsedTime());
		}
	}
}
