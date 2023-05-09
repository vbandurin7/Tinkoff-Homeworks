package ru.tinkoff.edu.java.scrapper.persistence.service;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.LinkInfoUpdater;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;



public abstract class AbstractLinkService implements LinkService {
    protected LinkRepository linkRepository;
    protected LinkInfoUpdater linkInfoUpdater;

    @Override
    public void save(LinkSaveRequest lr) {
        if (count(lr.getDtoLink().getUrl()) == 0) {
            linkInfoUpdater.update(lr);
            linkRepository.save(lr.getDtoLink());
        }
    }

    @Override
    public void delete(String url) {
        linkRepository.deleteByUrl(url);
    }

    @Override
    public LinkDto findByUrl(String url) {
        try {
            return linkRepository.findByUrl(url);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updateTime(LinkDto linkDto) {
        linkRepository.updateTime(linkDto);
    }

    @Override
    public List<LinkDto> findUnchecked() {
        return linkRepository.findUncheckedLinks();
    }

    @Override
    public long count(String url) {
        return linkRepository.countByUrl(url);
    }

    private <T> void setTime(Optional<T> response, Function<T, OffsetDateTime> f, LinkDto linkDto) {
        linkDto.setUpdatedAt(f.apply(response.get()));
        linkDto.setLastCheckedAt(OffsetDateTime.now());
    }
}
