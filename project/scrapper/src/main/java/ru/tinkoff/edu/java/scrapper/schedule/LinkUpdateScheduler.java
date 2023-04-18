package ru.tinkoff.edu.java.scrapper.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.linkParser.parser.LinkParser;
import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdateScheduler {
    private final LinkService linkService;
    private final UpdateHandler updateHandler;

    @Scheduled(fixedDelayString = "#{schedulerInterval}")
    public void update() {
        List<Link> links = linkService.findAll();
        links.stream().filter(link -> Timestamp.from(Instant.now()).getTime() - link.getLastCheckedAt().getTime() >= 300000)
                .forEach(link -> {
                    ParseResult parseResult = LinkParser.parseURL(link.getUrl());
                    updateHandler.handleUpdate(link, parseResult);
                });
    }
}

