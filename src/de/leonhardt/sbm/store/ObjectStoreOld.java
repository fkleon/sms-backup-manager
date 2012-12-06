package de.leonhardt.sbm.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import de.leonhardt.sbm.util.Utils.IdGenerator;



public abstract class ObjectStoreOld<A> {

	/**
	 * Logger
	 */
	protected Logger log;
	
	/**
	 * The objects stored in this ObjectStore
	 */
	protected Collection<A> col;
	
	/**
	 * Application-wide ID Generator
	 */
	protected IdGenerator idGen;
	
	/**
	 * Creates a new ObjectStore with given Id Generator.
	 * - Initializes internal collection to ArrayList
	 * - Initializes Logger
	 * 
	 * @param idGen
	 */
	public ObjectStoreOld(IdGenerator idGen) {
		this.idGen = idGen;
		this.col = new ArrayList<A>();
		this.log = Logger.getLogger("ObjectStore");
	}
	
	/**
	 * Adds the given object to the store.
	 * Before adding the object, it also assigns an ID to it.
	 * 
	 * @param obj
	 */
	public boolean addObject(A obj) {
		boolean success = col.add(assignId(obj));
		
		if (success) {
			log.finest("Sucessfully added object: " + obj);
		} else {
			log.finest("Object already in store: " + obj);
		}
		
		return success;
	}
	
	protected abstract A assignId(A obj);
	
	/**
	 * Removes all objects from this store.
	 */
	public void clearStore() {
		this.col.clear();
	}
	
	/**
	 * TODO to precisely remove objects from store, e.g. a selection of duplicates
	 */
	public void remove(A obj) {}
	public void removeAll(Collection<A> objs) {}
	
	/**
	 * 
	 * @param obj
	 * @return true, if element already in store
	 */
	public boolean contains(A obj) {
		return this.col.contains(obj);
	}
	
	/**
	 * @return the number of objects in this store
	 */
	public int getSize() {
		return this.col.size();
	}

}
