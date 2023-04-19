package ru.tinkoff.edu.java.scrapper.persistence.repository;

public interface CrudRepository<T, ID> {

    void deleteById(ID id);

    Iterable<T> findAll();

    T findById(ID id);

    T save(T entity);

    long countById(ID id);
}
