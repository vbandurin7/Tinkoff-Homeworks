package ru.tinkoff.edu.java.scrapper.persistence.repository;

public interface CrudRepository<T, ID> {

    void deleteById(ID id);

    Iterable<T> findAll();

    T findById(ID id);

    void save(T entity);

    long count(ID id);
}
