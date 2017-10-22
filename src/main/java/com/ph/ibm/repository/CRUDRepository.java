package com.ph.ibm.repository;

/**
 * @author Cyril Pangilinan
 *
 * Repository interface for generic CRUD operations
 * 
 * @param T the Entity type 
 * @param ID the ID type 
 */
public interface CRUDRepository<T, ID> {
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as
	 * the save operation might have changed the entity instance completely.
	 *
	 * @param entity
	 * @return the saved entity
	 */
	T saveOrUpdate(T entity);

	/**
	 * Saves all given entities.
	 *
	 * @param entities
	 * @return
	 */
	Iterable<T> saveOrUpdate(Iterable<? extends T> entities);

	/**
	 * Retrieves an entity by the given key
	 *
	 * @param id
	 * @return the entity with the given primary key or {@code null} if none
	 *         found
	 */
	T findByID(ID id);

	/**
	 * Returns whether an entity with the given id exists.
	 *
	 * @param id
	 * @return true if an entity with the given id exists, alse otherwise
	 */
	boolean exists(ID id);

	/**
	 * Returns the number of entities available.
	 *
	 * @return the number of entities
	 */
	long count();

	/**
	 * Deletes the entity with the given id.
	 * 
	 * @param id
	 */
	void deleteByID(ID id);

	/**
	 * Deletes a given entity.
	 *
	 * @param entity
	 */
	void delete(T entity);

	/**
	 * Deletes the given entities.
	 *
	 * @param entities
	 */
	void delete(Iterable<? extends T> entities);
}
