package ru.tinkoff.edu.java.scrapper.persistence.repository;

import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;

import java.util.List;

public interface LinkRepository extends CrudRepository<LinkDto, Long> {

    LinkDto findByUrl(String url);

    void deleteByUrl(String url);

    List<LinkDto> findUncheckedLinks();

    void updateTime(LinkDto linkDto);

    long countByUrl(String url);
}