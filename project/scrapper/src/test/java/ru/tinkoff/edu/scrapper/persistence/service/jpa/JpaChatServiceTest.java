package ru.tinkoff.edu.scrapper.persistence.service.jpa;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaChatService;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;

import java.time.OffsetDateTime;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.*;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JpaChatServiceTest {

    private static final Chat enityTestChat = new Chat(1L, new HashSet<>());

    private static final ChatSaveRequest chr = new ChatSaveRequest(
            new ru.tinkoff.edu.java.scrapper.persistence.dto.Chat(1L), enityTestChat);

    private static final ru.tinkoff.edu.java.scrapper.persistence.entity.Link entityTestLink
            = new ru.tinkoff.edu.java.scrapper.persistence.entity.Link(TEST_URL, new HashSet<>(),
            LINK_INFO, OffsetDateTime.now(), OffsetDateTime.now());

    static {
        enityTestChat.getLinks().add(entityTestLink);
        entityTestLink.getChats().add(enityTestChat);
    }
    @Autowired
    private JpaChatService jpaChatService;

    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Autowired
    private JpaChatRepository jpaChatRepository;

    @BeforeEach
    public void clearDB() {
        jpaChatRepository.deleteAll();
    }
    @BeforeAll
    public void beforeAll() {
        jpaLinkRepository.save(entityTestLink);
    }
    @Test
    void register_chatNotExists_chatRegistered() {
        assertThat(jpaChatService.register(chr)).isEqualTo(chr.getDtoChat());
    }

    @Test
    void register_chatExists_chatReturned() {
        assertThat(jpaChatService.register(chr)).isEqualTo(chr.getDtoChat());
    }

    @Test
    void unregister_chatExists_chatDeleted() {
        //given
        jpaChatRepository.save(chr.getEntityChat());

        //when
        jpaChatService.unregister(chr.getDtoChat().getId());
        Long count = jpaChatRepository.countById(chr.getDtoChat().getId());

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    void unregister_chatNotExists_nothingHappened() {
        //when
        jpaChatService.unregister(chr.getDtoChat().getId());
        Long count = jpaChatRepository.countById(chr.getDtoChat().getId());

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    void count_chatExists_returnOne() {
        //given
        jpaChatRepository.save(chr.getEntityChat());

        //when
        long count = jpaChatService.count(chr.getDtoChat().getId());

        //then
        assertThat(count).isEqualTo(1);
    }
}
