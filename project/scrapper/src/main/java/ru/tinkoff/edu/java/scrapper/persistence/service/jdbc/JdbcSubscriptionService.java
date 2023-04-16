package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.SubscriptionService;


import java.net.URI;

@Service
@RequiredArgsConstructor
public class JdbcSubscriptionService implements SubscriptionService {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcLinkService jdbcLinkService;
    private final JdbcChatService jdbcChatService;


    @Override
    @Transactional
    public Link addLink(long tgChatId, URI url) {
        Link link = new Link(url);
        jdbcLinkService.save(link);
        jdbcChatService.save(new Chat(tgChatId));
        jdbcTemplate.update("INSERT into chat_link VALUES (?, ?)", tgChatId, link.getId());
        return link;
    }

    @Override
    @Transactional
    public Link removeLink(long tgChatId, URI url) {
        Link link = jdbcLinkService.findByUrl(url.toString());
        jdbcTemplate.update("DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?", tgChatId, link.getId());
        if (countLinkTracks(link.getId()) == 0) {
            jdbcLinkService.deleteById(link.getId());
        }
        if (countChatTracks(tgChatId) == 0) {
            jdbcChatService.deleteById(tgChatId);
        }
        return link;
    }

    private long countLinkTracks(Long id) {
        Long count = jdbcTemplate.queryForObject("SELECT count(*) FROM chat_link WHERE link_id = ?", Long.class, id);
        return count == null ? 0 : count;
    }

    private long countChatTracks(Long id) {
        Long count = jdbcTemplate.queryForObject("SELECT count(*) FROM chat_link WHERE chat_id = ?", Long.class, id);
        return count == null ? 0 : count;
    }
}
