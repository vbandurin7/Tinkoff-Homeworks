package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;

import java.util.List;

public interface LinkService {
    void save(Link link);
    void delete(String url);
    Link findByUrl(String url);
    void updateTime(Link link);
    long count(String url);
    List<Link> findUnchecked();
}