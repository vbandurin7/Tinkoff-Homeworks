package ru.tinkoff.edu.scrapper.persistence.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.exception.ChatAlreadyExistsException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.ChatRepositoryImpl;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.scrapper.IntegrationEnvironment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestProvider.*;

public class JdbcChatServiceTest extends IntegrationEnvironment {

    private static final Chat TEST_CHAT = new Chat(1);
    static JdbcChatService jdbcChatService;
    static JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void init() {
        jdbcTemplate = new JdbcTemplate(DataSourceBuilder.create()
                .url(POSTGRE_SQL_CONTAINER.getJdbcUrl())
                .username(POSTGRE_SQL_CONTAINER.getUsername())
                .password(POSTGRE_SQL_CONTAINER.getPassword())
                .build());
        jdbcChatService = new JdbcChatService(new ChatRepositoryImpl(jdbcTemplate));
    }

    @AfterEach
    public void clearDB() {
        jdbcTemplate.update(CLEAR_CHAT_SQL);
    }

    @Test
    void register_chatNotExists_chatRegistered() {
        assertThat(jdbcChatService.register(TEST_CHAT)).isEqualTo(TEST_CHAT);
    }

    @Test
    void register_chatExists_chatReturned() {
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT.getId());
        assertThat(jdbcChatService.register(TEST_CHAT)).isEqualTo(TEST_CHAT);
    }

    @Test
    void unregister_chatExists_chatDeleted() {
        //given
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT.getId());

        //when
        jdbcChatService.unregister(TEST_CHAT.getId());
        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_SQL, Long.class, TEST_CHAT.getId());

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    void unregister_chatNotExists_nothingHappened() {
        //when
        jdbcChatService.unregister(TEST_CHAT.getId());
        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_SQL, Long.class, TEST_CHAT.getId());

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    void count_chatExists_returnOne() {
        //given
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT.getId());

        //when
        long count = jdbcChatService.count(TEST_CHAT.getId());

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void count_linkNotExist_returnZero() {
        assertThat(jdbcChatService.count(TEST_CHAT.getId())).isEqualTo(0);
    }
}
