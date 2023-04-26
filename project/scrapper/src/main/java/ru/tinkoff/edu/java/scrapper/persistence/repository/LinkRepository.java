package ru.tinkoff.edu.java.scrapper.persistence.repository;

import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;

import java.util.List;

public interface LinkRepository extends CrudRepository<Link, Long> {

    Link findByUrl(String url);

    void deleteByUrl(String url);

    List<Link> findUncheckedLinks();

    void updateTime(Link link);

    long countByUrl(String url);
}