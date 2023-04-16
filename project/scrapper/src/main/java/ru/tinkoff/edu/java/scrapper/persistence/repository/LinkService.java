package ru.tinkoff.edu.java.scrapper.persistence.repository;

import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.util.List;

public interface LinkService extends CrudRepository<Link, Long> {

    Link findByUrl(String url);

    List<Link> findAllByChat(long chatId);

    Link deleteByUrl(String url);

}
