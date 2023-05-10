package ru.tinkoff.edu.java.scrapper.persistence.repository;

public interface CrudRepository<T, I> {

    T save(T entity);

    long countById(I id);
}
