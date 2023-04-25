package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository jpaLinkRepository;

    @Override
    public void save(Link link) {
    }

    @Override
    public void delete(String url) {

    }

    @Override
    public ru.tinkoff.edu.java.scrapper.persistence.dto.Link findByUrl(String url) {
        return null;
    }

    @Override
    public void updateTime(Link link) {
        jpaLinkRepository.updateTime(link);
    }

    @Override
    public long count(String url) {
        return 0;
    }

    @Override
    public List<Link> findUnchecked() {
        return null;
    }

    private ru.tinkoff.edu.java.scrapper.persistence.entity.Link dtoToEntityLink(Link link) {
        return new Link(link.getId(), );
    }
}
