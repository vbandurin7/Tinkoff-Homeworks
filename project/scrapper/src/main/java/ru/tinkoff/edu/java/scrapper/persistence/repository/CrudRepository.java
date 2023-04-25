package ru.tinkoff.edu.java.scrapper.persistence.repository;

public interface CrudRepository<T, ID> {

    T save(T entity);

    long countById(ID id);
}
