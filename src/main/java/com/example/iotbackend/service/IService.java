package com.example.iotbackend.service;

import java.util.List;
import java.util.Optional;

/**
 * IService is a generic interface that defines the basic CRUD (Create, Read, Update, Delete) operations
 * for services that manage entities in the application. This interface is intended to be extended by
 * specific service interfaces that operate on specific entities.
 *
 * @param <T> The type of the entity (e.g., Account, Product, etc.)
 * @param <ID> The type of the entity's identifier (e.g., Long, String, etc.)
 */
public interface IService<T, ID> {

    /**
     * Saves the given entity to the database.
     *
     * @param entity The entity to be saved.
     * @return The saved entity, typically with an ID assigned if it's a new entity.
     */
    T save(T entity);

    /**
     * Deletes an entity by its ID.
     *
     * @param id The ID of the entity to be deleted.
     */
    void delete(ID id);

    /**
     * Retrieves all entities of type T from the database.
     *
     * @return A list of all entities of type T.
     */
    List<T> findAll();

    /**
     * Retrieves an entity by its ID.
     *
     * @param id The ID of the entity to retrieve.
     * @return An Optional containing the entity if found, or empty if not found.
     */
    Optional<T> findById(ID id);
}
