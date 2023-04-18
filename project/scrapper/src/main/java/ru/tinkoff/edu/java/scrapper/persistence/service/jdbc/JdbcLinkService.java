package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JdbcLinkService implements LinkService {

    private final LinkRepository linkRepository;

    @Override
    public void save(Link link) {
        // оставим валидацию ссылки на бота (добавлю чуть позже)
        if (count(link.getUrl().toString()) == 0) {
            linkRepository.save(link);
        }
    }

    @Override
    public void delete(URI url) {
        linkRepository.deleteByUrl(url.toString());
    }

    @Override
    public Link findByUrl(String url) {
        try {
            return linkRepository.findByUrl(url);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updateTime(Link link) {
        linkRepository.updateTime(link);
    }

    @Override
    public List<Link> findAll() {
        return null;
    }

    @Override
    public long count(String url) {
        return linkRepository.countByUrl(url);
    }
}
