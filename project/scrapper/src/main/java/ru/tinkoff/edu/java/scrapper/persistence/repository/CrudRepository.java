package ru.tinkoff.edu.java.scrapper.persistence.repository;

import org.springframework.stereotype.Component;

public interface CrudRepository<T, ID> {

    void deleteById(ID id);

    Iterable<T> findAll();

    T findById(ID id);

    T save(T entity);

    long count(ID id);
}
