package com.example.iotbackend.service.impl;

import com.example.iotbackend.exception.ResourceNotFoundException;
import com.example.iotbackend.service.IService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * AbstractService provides a base implementation for CRUD operations.
 * It is designed to be extended by other services that need to interact with a JPA repository.
 *
 * @param <T>  The type of the entity (e.g., Account, Product, etc.)
 * @param <ID> The type of the entity's ID (e.g., Long, Integer, etc.)
 */
public abstract class AbstractService<T, ID> implements IService<T, ID> {

    /**
     * Returns the repository to be used for this service.
     * This method must be implemented by subclasses to return the specific repository for the entity.
     */
    protected abstract JpaRepository<T, ID> getRepository();

    /**
     * Saves the given entity to the database.
     * @param entity The entity to be saved.
     * @return The saved entity.
     */
    @Override
    public T save(T entity) {
        return getRepository().save(entity);
    }

    /**
     * Deletes an entity by its ID.
     * Throws a ResourceNotFoundException if the entity does not exist.
     * @param id The ID of the entity to be deleted.
     */
    @Override
    public void delete(ID id) {
        if (!getRepository().existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with ID: " + id);  // Throws exception if the entity doesn't exist.
        }
        getRepository().deleteById(id);
    }

    /**
     * Returns all entities from the database.
     * @return A list of all entities.
     */
    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    /**
     * Returns an entity by its ID.
     * @param id The ID of the entity to be retrieved.
     * @return An Optional containing the entity, or an empty Optional if not found.
     */
    @Override
    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }
}
