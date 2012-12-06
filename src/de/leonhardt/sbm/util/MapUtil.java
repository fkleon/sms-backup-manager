package de.leonhardt.sbm.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * some handy map utilities
 * 
 * @author Frederik Leonhardt <frederik.leonhardt@dfki.de>
 * @date 15.03.2012
 *
 */
public class MapUtil {
	
	/**
	 * order of sorting 
	 * 
	 * @author Frederik Leonhardt <frederik.leonhardt@dfki.de>
	 * @date 16.03.2012
	 *
	 */
	public enum Order {
		ASCENDING,
		DESCENDING;
	}

	/**
	 * sorts a map by values,
	 * highest first!
	 * 
	 * @param map
	 * @return sorted map
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		return sortByValue(map, Order.DESCENDING);
	}
	
	/**
	 * sorts a map by values,
	 * by the Order given
	 * 
	 * @param map
	 * @return sorted map
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map, final Order order) {
		
		// get list of entries
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
				
		
		Comparator<Map.Entry<K, V>> valueComparator = 
			
			/**
			 * value comparator
			 */
			new Comparator<Map.Entry<K, V>>() {
				// compare by value
				public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
					if (Order.ASCENDING.equals(order)) {
						return (o1.getValue()).compareTo(o2.getValue());
					}
					else {
						return (o2.getValue()).compareTo(o1.getValue());						
					}
				}
			};
		
		// sort list of entries by a value comparator
		Collections.sort(list,valueComparator);

		// generate result map
		Map<K, V> result = new LinkedHashMap<K, V>();
		
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		
		return result;
	}
	
	/**
	 * dumps all entries of a map to console output
	 * 
	 * @param map
	 */
	public static <K,V> void dumpMap(Map<K, V> map) {
		if (map == null) {
			System.out.println("CAN NOT DUMP null MAP!");
			return;
		}
		
		System.out.println("> DUMPING MAP - SIZE: "+map.size());
		
		int i = 1;
		for (Entry<K, V> e: map.entrySet()) {
			System.out.println("Entry no. "+(i++)+": "+e.getKey()+" , "+e.getValue());
		}
		
		System.out.println("> DONE. EOM");

	}

	/**
	 * Creates a map out of given keys and values.
	 * All keys have to be of the same type. Same for the values.
	 * Key and Value arrays have to be same length.
	 * 
	 * @param keys
	 * @param values
	 * @return
	 */
	public static <K,V> Map<K,V> createMap(K[] keys, V[] values) {
		Map<K,V> map = new HashMap<K, V>();
		
		int noEntries = keys.length > values.length ? keys.length : values.length;
		
		for(int i = 0; i<noEntries; i++) {
			map.put(keys[i], values[i]);
		}
		
		return map;
	}
	
}
