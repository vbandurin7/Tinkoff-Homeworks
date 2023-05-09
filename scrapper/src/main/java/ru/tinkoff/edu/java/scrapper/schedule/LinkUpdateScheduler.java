package ru.tinkoff.edu.java.scrapper.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.linkParser.parser.LinkParser;
import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdateScheduler {

    private final LinkService linkService;
    private final UpdateHandler updateHandler;

    @Scheduled(fixedDelayString = "#{schedulerInterval}")
    public void update() {
        List<LinkDto> linkDtos = linkService.findUnchecked();
        linkDtos.forEach(link -> {
            ParseResult parseResult = LinkParser.parseURL(URI.create(link.getUrl()));
            updateHandler.handleUpdate(link, parseResult);
            linkService.updateTime(link);
        });
    }
}

