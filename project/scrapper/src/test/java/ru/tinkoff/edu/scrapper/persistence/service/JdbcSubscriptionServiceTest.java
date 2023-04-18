package ru.tinkoff.edu.scrapper.persistence.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.ChatRepositoryImpl;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepositoryImpl;
import ru.tinkoff.edu.java.scrapper.persistence.repository.SubscriptionRepositoryImpl;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcSubscriptionService;
import ru.tinkoff.edu.scrapper.IntegrationEnvironment;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestProvider.*;

public class JdbcSubscriptionServiceTest extends IntegrationEnvironment {

    private static final Chat TEST_CHAT = new Chat(1);
    private static final String TEST_URL = "testUrl";
    private static final String TEST_URL_2= "testUrl2";

    static JdbcSubscriptionService jdbcSubscriptionService;
    static JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void init() {
        jdbcTemplate = new JdbcTemplate(DataSourceBuilder.create()
                .url(POSTGRE_SQL_CONTAINER.getJdbcUrl())
                .username(POSTGRE_SQL_CONTAINER.getUsername())
                .password(POSTGRE_SQL_CONTAINER.getPassword())
                .build());
        jdbcSubscriptionService = new JdbcSubscriptionService(
                new JdbcLinkService(new LinkRepositoryImpl(jdbcTemplate)),
                new JdbcChatService(new ChatRepositoryImpl(jdbcTemplate)),
                new SubscriptionRepositoryImpl(jdbcTemplate));
    }

    @BeforeEach
    public void clearDB() {
        jdbcTemplate.update(CLEAR_CHAT_LINK_SQL);
        jdbcTemplate.update(CLEAR_LINK_SQL);
        jdbcTemplate.update(CLEAR_CHAT_SQL);
    }

    @Test
    void addLink_relationNotExist_relationAdded() {

        //when
        jdbcSubscriptionService.addLink(TEST_CHAT.getId(), URI.create(TEST_URL));
        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_LINK_SQL, Long.class, TEST_URL, TEST_CHAT.getId());

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void addLink_relationExists_nothingHappened() {
        //given
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL);
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT.getId());
        jdbcTemplate.update(INSERT_CHAT_LINK_SQL, TEST_CHAT.getId(), TEST_URL);

        //when
        jdbcSubscriptionService.addLink(TEST_CHAT.getId(), URI.create(TEST_URL));
        Long count = jdbcTemplate.queryForObject(
                COUNT_CHAT_LINK_SQL, Long.class, TEST_URL, TEST_CHAT.getId());

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void removeLink_relationExists_linkRemoved() {
        //given
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL);
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT.getId());
        jdbcTemplate.update("INSERT INTO chat_link VALUES (?,?)", TEST_CHAT.getId(), TEST_URL);

        //when
        jdbcSubscriptionService.removeLink(TEST_CHAT.getId(), URI.create(TEST_URL));
        Long count = jdbcTemplate.queryForObject(
                COUNT_CHAT_LINK_SQL, Long.class, TEST_URL, TEST_CHAT.getId());

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    void removeLink_relationNotExist_nothingHappened() {
        //given
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL);
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT.getId());

        //when
        jdbcSubscriptionService.removeLink(TEST_CHAT.getId(), URI.create(TEST_URL));
        Long count = jdbcTemplate.queryForObject(
                COUNT_CHAT_LINK_SQL, Long.class, TEST_URL, TEST_CHAT.getId());

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    void findAll_chatTracksSomeLinks_listOfLinksReturned() {
        //given
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL);
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL_2);
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT.getId());
        jdbcTemplate.update(INSERT_CHAT_LINK_SQL, TEST_CHAT.getId(), TEST_URL_2);
        jdbcTemplate.update(INSERT_CHAT_LINK_SQL, TEST_CHAT.getId(), TEST_URL);

        //when
        final List<Link> links = jdbcSubscriptionService.listAll(TEST_CHAT.getId());
        List<String> expectedLinks = List.of(TEST_URL, TEST_URL_2);

        //then
        assertThat(links.size()).isEqualTo(2);
        assertTrue(expectedLinks.contains(links.get(0).getUrl().toString()));
        assertTrue(expectedLinks.contains(links.get(1).getUrl().toString()));
    }

    @Test
    void findAll_chatDoesNotTrackLinks_noRelations() {
        //when
        final List<Link> links = jdbcSubscriptionService.listAll(TEST_CHAT.getId());

        //then
        assertThat(links.size()).isEqualTo(0);
    }
}
