package ru.tinkoff.edu.scrapper.persistence.service.jooq;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jooq.JooqChatService;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JooqChatServiceTest {

    private static final ChatSaveRequest TEST_CHAT = new ChatSaveRequest(new ChatDto(1), null);

    @Autowired
    private JooqChatService jooqChatService;
    @Autowired
    private JooqLinkRepository jooqLinkRepository;
    @Autowired
    private JooqSubscriptionRepository jooqSubscriptionRepository;
    @Autowired
    private JooqChatRepository jooqChatRepository;

    @BeforeAll
    public void prepDB() {
        jooqSubscriptionRepository.deleteAll();
        jooqLinkRepository.deleteAll();
        jooqChatRepository.deleteAll();
    }

    @AfterEach
    public void clearDB() {
        jooqChatRepository.deleteAll();
    }

    @Test
    void register_chatNotExists_chatRegistered() {
        assertThat(jooqChatService.register(TEST_CHAT)).isEqualTo(TEST_CHAT.getDtoChat());
    }

    @Test
    void register_chatExists_chatReturned() {
        jooqChatRepository.save(TEST_CHAT.getDtoChat());
        assertThat(jooqChatService.register(TEST_CHAT)).isEqualTo(TEST_CHAT.getDtoChat());
    }

    @Test
    void unregister_chatExists_chatDeleted() {
        //given
        jooqChatRepository.save(TEST_CHAT.getDtoChat());

        //when
        jooqChatService.unregister(TEST_CHAT.getDtoChat().getId());
        Long count = jooqChatRepository.countById(TEST_CHAT.getDtoChat().getId());

        //then
        assertThat(count).isZero();
    }

    @Test
    void unregister_chatNotExists_nothingHappened() {
        //when
        jooqChatService.unregister(TEST_CHAT.getDtoChat().getId());
        Long count = jooqChatRepository.countById(TEST_CHAT.getDtoChat().getId());

        //then
        assertThat(count).isZero();
    }

    @Test
    void count_chatExists_returnOne() {
        //given
        jooqChatRepository.save(TEST_CHAT.getDtoChat());

        //when
        long count = jooqChatService.count(TEST_CHAT.getDtoChat().getId());

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void count_linkNotExist_returnZero() {
        assertThat(jooqChatService.count(TEST_CHAT.getDtoChat().getId())).isZero();
    }
}
