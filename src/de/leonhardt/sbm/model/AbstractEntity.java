package de.leonhardt.sbm.model;

/**
 * An abstract entity is an entity with ID.
 * All entities used by the system should inherit from AbstractEntity.
 * 
 * @author Frederik Leonhardt
 *
 */
public abstract class AbstractEntity {

	/**
	 * Internal id of entity
	 */
	protected long id;
	
	/**
	 * Creates new AbstractEntity with given Id.
	 * @param id
	 */
	public AbstractEntity(long id) {
		this.id = id;
	}

	/**
	 * Returns ID of this entity.
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets ID of this entity.
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
}
