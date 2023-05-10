package ru.tinkoff.edu.scrapper.persistence.service.jdbc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.*;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@ActiveProfiles("test")
class JdbcChatServiceTest extends IntegrationEnvironment {

    private static final ChatSaveRequest TEST_CHAT = new ChatSaveRequest(new ChatDto(1), null);

    @Autowired
    private JdbcChatService jdbcChatService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void clearDB() {
        jdbcTemplate.update(CLEAR_CHAT_SQL);
    }

    @Test
    void register_chatNotExists_chatRegistered() {
        assertThat(jdbcChatService.register(TEST_CHAT)).isEqualTo(TEST_CHAT.getDtoChat());
    }

    @Test
    void register_chatExists_chatReturned() {
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT.getDtoChat().getId());
        assertThat(jdbcChatService.register(TEST_CHAT)).isEqualTo(TEST_CHAT.getDtoChat());
    }

    @Test
    void unregister_chatExists_chatDeleted() {
        //given
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT.getDtoChat().getId());

        //when
        jdbcChatService.unregister(TEST_CHAT.getDtoChat().getId());
        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_SQL, Long.class, TEST_CHAT.getDtoChat().getId());

        //then
        assertThat(count).isZero();
    }

    @Test
    void unregister_chatNotExists_nothingHappened() {
        //when
        jdbcChatService.unregister(TEST_CHAT.getDtoChat().getId());
        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_SQL, Long.class, TEST_CHAT.getDtoChat().getId());

        //then
        assertThat(count).isZero();
    }

    @Test
    void count_chatExists_returnOne() {
        //given
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT.getDtoChat().getId());

        //when
        long count = jdbcChatService.count(TEST_CHAT.getDtoChat().getId());

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void count_linkNotExist_returnZero() {
        assertThat(jdbcChatService.count(TEST_CHAT.getDtoChat().getId())).isZero();
    }
}
