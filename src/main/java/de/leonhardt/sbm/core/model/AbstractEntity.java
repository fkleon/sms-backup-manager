package de.leonhardt.sbm.core.model;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
