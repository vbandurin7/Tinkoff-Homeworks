package ru.tinkoff.edu.java.scrapper.persistence.repository;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.util.List;


@Component
public interface LinkRepository extends CrudRepository<Link, Long> {

    Link findByUrl(String url);

    void deleteByUrl(String url);

    List<Link> findUncheckedLinks();

    void updateTime(Link link);

    long countByUrl(String url);
}