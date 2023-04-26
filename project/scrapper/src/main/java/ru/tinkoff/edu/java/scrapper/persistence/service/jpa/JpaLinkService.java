package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.EntityConverter;

import java.util.List;

import static ru.tinkoff.edu.java.scrapper.persistence.service.utils.EntityConverter.entityToDtoLink;


@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository jpaLinkRepository;
    private final long checkInterval;

    @Override
    public void save(LinkSaveRequest lr) {
        if (count(lr.getDtoLink().getUrl()) == 0) {
            jpaLinkRepository.save(lr.getEntityLink());
        }
    }

    @Override
    public void delete(String url) {
        jpaLinkRepository.deleteByUrl(url);
    }

    @Override
    public Link findByUrl(String url) {
        var entityLink = jpaLinkRepository.findByUrl(url);
        return entityLink == null ? null : entityToDtoLink(entityLink);
    }

    @Override
    public void updateTime(Link link) {
        jpaLinkRepository.updateTime(link.getLastCheckedAt(), link.getUpdatedAt(), link.getUrl());
    }

    @Override
    public long count(String url) {
        return jpaLinkRepository.countByUrl(url);
    }

    @Override
    public List<Link> findUnchecked() {
        return jpaLinkRepository.findUnchecked(checkInterval).stream().map(EntityConverter::entityToDtoLink).toList();
    }
}
