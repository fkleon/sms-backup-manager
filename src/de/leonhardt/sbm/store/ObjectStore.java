package de.leonhardt.sbm.store;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import de.leonhardt.sbm.util.Utils.IdGenerator;



public abstract class ObjectStore<E> extends AbstractCollection<E> {

	/**
	 * Logger
	 */
	protected Logger log;
	
	/**
	 * The objects stored in this ObjectStore
	 */
	protected Collection<E> col;
	
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
	public ObjectStore(IdGenerator idGen) {
		this.idGen = idGen;
		this.col = new ArrayList<E>();
		this.log = Logger.getLogger("ObjectStore");
	}
	
	/**
	 * Creates a new ObjectStore with given Id Generator and collection.
	 * - Initializes Logger
	 * 
	 * @param idGen
	 */
	public ObjectStore(IdGenerator idGen, Collection<E> col) {
		this.idGen = idGen;
		this.col = col;
		this.log = Logger.getLogger("ObjectStore");
	}
	
	/**
	 * Adds the given object to the store.
	 * Before adding the object, it also assigns an ID to it.
	 * 
	 * @param obj
	 */
	public boolean addObject(E obj) {
		boolean success = col.add(assignId(obj));
		
		if (success) {
			log.finest("Sucessfully added object: " + obj);
		} else {
			log.finest("Object already in store: " + obj);
		}
		
		return success;
	}
	
	protected abstract E assignId(E obj);
	
	public boolean add(E e) {
		return col.add(e);
	}

	@Override
	public Iterator<E> iterator() {
		return col.iterator();
	}

	@Override
	public int size() {
		return col.size();
	}

}
