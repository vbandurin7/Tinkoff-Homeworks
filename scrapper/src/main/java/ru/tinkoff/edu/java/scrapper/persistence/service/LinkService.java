package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;

import java.util.List;

public interface LinkService {
    void save(LinkSaveRequest linkSaveRequest);
    void delete(String url);
    LinkDto findByUrl(String url);
    void updateTime(LinkDto linkDto);
    long count(String url);
    List<LinkDto> findUnchecked();
}