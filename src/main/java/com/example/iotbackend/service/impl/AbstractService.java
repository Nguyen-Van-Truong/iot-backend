package com.example.iotbackend.service.impl;

import com.example.iotbackend.exception.ResourceNotFoundException;
import com.example.iotbackend.service.IService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractService<T, ID> implements IService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    @Override
    public T save(T entity) {
        return getRepository().save(entity);
    }

    @Override
    public void delete(ID id) {
        if (!getRepository().existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with ID: " + id);
        }
        getRepository().deleteById(id);
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }
}
