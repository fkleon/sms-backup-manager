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
	//protected IdGenerator idGen;
	
	/**
	 * Creates a new ObjectStore with given Id Generator.
	 * - Initializes internal collection to ArrayList
	 * - Initializes Logger
	 * 
	 * @param idGen
	 */
	public ObjectStore() {
//		this.idGen = idGen;
		this.col = new ArrayList<E>();
		this.log = Logger.getLogger("ObjectStore");
	}
	
	/**
	 * Creates a new ObjectStore with given Id Generator and collection.
	 * - Initializes Logger
	 * 
	 * @param idGen
	 */
	public ObjectStore(Collection<E> col) {
//		this.idGen = idGen;
		this.col = col;
		this.log = Logger.getLogger("ObjectStore");
	}
	
	/**
	 * Adds the given object to the store.
	 * Before adding the object, it also assigns an ID to it.
	 * 
	 * @param obj
	 * @return true, if object was added successfully to the store
	 */
	protected boolean addObject(E obj) {
		if (col.contains(obj)) {
			//log.info("Object already in store: " + obj);
			//log.info("coldump: " + col);
			return false;
		}
		
//		boolean success = col.add(assignId(obj));
		return col.add(obj);
//		log.finest("Sucessfully added object: " + obj);

//		return success;
	}
	
//	protected abstract E assignId(E obj);
	
	public boolean add(E e) {
		return addObject(e);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> col) {
		return super.addAll(col);
	}
	
	/**
	 * Finds the reference to a given object in the store.
	 * 
	 * @param objectToFind
	 * @return null, if this object is not in store
	 */
	protected E find(Object objectToFind) {
		//System.out.println("finding "+objectToFind);
		for (E obj: this) {
			if (obj.equals(objectToFind)) {
				//System.out.println("! this: "+obj);
				return obj;
			}
		}
		return null;
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
