package com.example.iotbackend.service;

import java.util.List;
import java.util.Optional;

public interface IService<T, ID> {
    T save(T entity);
    void delete(ID id);
    List<T> findAll();
    Optional<T> findById(ID id);
}
