package ru.tinkoff.edu.scrapper.persistence.service.jpa;


import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaSubscriptionService;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.*;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JpaSubscriptionServiceTest {

    private static final Chat enityTestChat = new Chat(1L, new HashSet<>());

    private static final ru.tinkoff.edu.java.scrapper.persistence.entity.Link entityTestLink
            = new ru.tinkoff.edu.java.scrapper.persistence.entity.Link(TEST_URL, new HashSet<>(),
            LINK_INFO, OffsetDateTime.now(), OffsetDateTime.now());

    private static final ru.tinkoff.edu.java.scrapper.persistence.entity.Link entityTestLink2
            = new ru.tinkoff.edu.java.scrapper.persistence.entity.Link(TEST_URL_2, new HashSet<>(),
            LINK_INFO_2, OffsetDateTime.now(), OffsetDateTime.now());

    static {
        enityTestChat.getLinks().add(entityTestLink);
        entityTestLink.getChats().add(enityTestChat);
    }

    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Autowired
    private JpaSubscriptionService jpaSubscriptionService;

    @AfterEach
    @SneakyThrows
    public void clearDB() {
        try {
            jpaSubscriptionService.removeLink(enityTestChat.getId(), entityTestLink.getUrl());
        } catch (Exception ignored) {
        }
    }

    @Test
    @Transactional
    void addLink_relationNotExist_relationAdded() {

        //when
        jpaSubscriptionService.addLink(TEST_CHAT.getId(), TEST_URL);
        long countLink = jpaLinkRepository.countByUrl(TEST_URL);
        long countChat = jpaChatRepository.countById(TEST_CHAT.getId());

        //then
        assertThat(countLink).isEqualTo(1);
        assertThat(countChat).isEqualTo(1);
    }

    @Test
    @Transactional
    void addLink_relationExists_nothingHappened() {
        //given
        jpaSubscriptionService.addLink(TEST_CHAT.getId(), TEST_URL);

        //when
        jpaSubscriptionService.addLink(TEST_CHAT.getId(), TEST_URL);
        long countLink = jpaLinkRepository.countByUrl(TEST_URL);
        long countChat = jpaChatRepository.countById(TEST_CHAT.getId());

        //then
        assertThat(countLink).isEqualTo(1);
        assertThat(countChat).isEqualTo(1);
    }

    @Test
    @Transactional
    void removeLink_relationExists_linkRemoved() {
        //given
        jpaSubscriptionService.addLink(TEST_CHAT.getId(), TEST_URL);

        //when
        jpaSubscriptionService.removeLink(TEST_CHAT.getId(), TEST_URL);
        long countLink = jpaLinkRepository.countByUrl(TEST_URL);
        long countChat = jpaChatRepository.countById(TEST_CHAT.getId());

        //then
        assertThat(countLink).isEqualTo(0);
        assertThat(countChat).isEqualTo(0);
    }

    @Test
    void removeLink_relationNotExist_exceptionThrown() {
        assertThrows(LinkNotFoundException.class, () -> jpaSubscriptionService.removeLink(TEST_CHAT.getId(), TEST_URL));
    }

    @Test
    @Transactional
    void listAll_chatTracksSomeLinks_listOfLinksReturned() {
        //given
        jpaSubscriptionService.addLink(enityTestChat.getId(), entityTestLink.getUrl());
        jpaSubscriptionService.addLink(enityTestChat.getId(), entityTestLink2.getUrl());

        //when
        final List<Link> links = jpaSubscriptionService.listAll(TEST_CHAT.getId());
        List<String> expectedLinks = List.of(TEST_URL, TEST_URL_2);

        //then
        assertThat(links.size()).isEqualTo(2);
        assertTrue(expectedLinks.contains(links.get(0).getUrl()));
        assertTrue(expectedLinks.contains(links.get(1).getUrl()));
    }

    @Test
    void listAll_chatDoesNotTrackLinks_exceptionThrown() {
        assertThrows(ChatNotFoundException.class, () -> jpaSubscriptionService.listAll(TEST_CHAT.getId()));
    }

    @Test
    @Transactional
    void chatList_relationExists_listReturned() {
        //given
        jpaSubscriptionService.addLink(enityTestChat.getId(), entityTestLink.getUrl());
        jpaSubscriptionService.addLink(enityTestChat.getId(), entityTestLink2.getUrl());

        //when
        final List<ru.tinkoff.edu.java.scrapper.persistence.dto.Chat> chats = jpaSubscriptionService.chatList(TEST_URL);

        //then
        assertThat(chats.size()).isEqualTo(1);
        assertThat(chats.get(0)).isEqualTo(TEST_CHAT);
    }

    @Test
    void chatList_noRelation_exceptionThrown() {
        assertThrows(LinkNotFoundException.class, () -> jpaSubscriptionService.chatList(TEST_URL));
    }
}
