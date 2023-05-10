package ru.tinkoff.edu.scrapper.persistence.service.jpa;


import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaSubscriptionService;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.*;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class JpaSubscriptionServiceTest {

    private static final Chat ENITY_TEST_CHAT = new Chat(1L, new HashSet<>());

    private static final ru.tinkoff.edu.java.scrapper.persistence.entity.Link ENTITY_TEST_LINK
            = new ru.tinkoff.edu.java.scrapper.persistence.entity.Link(TEST_URL, new HashSet<>(),
            LINK_INFO, OffsetDateTime.now(), OffsetDateTime.now());

    private static final ru.tinkoff.edu.java.scrapper.persistence.entity.Link ENTITY_TEST_LINK_2
            = new ru.tinkoff.edu.java.scrapper.persistence.entity.Link(TEST_URL_2, new HashSet<>(),
            LINK_INFO_2, OffsetDateTime.now(), OffsetDateTime.now());

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
            jpaSubscriptionService.removeLink(ENITY_TEST_CHAT.getId(), ENTITY_TEST_LINK.getUrl());
        } catch (Exception ignored) {
        }
    }

    @Test
    @Transactional
    void addLink_relationNotExist_relationAdded() {

        //when
        jpaSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);
        long countLink = jpaLinkRepository.countByUrl(TEST_URL);
        long countChat = jpaChatRepository.countById(TEST_CHAT_DTO.getId());

        //then
        assertThat(countLink).isEqualTo(1);
        assertThat(countChat).isEqualTo(1);
    }

    @Test
    @Transactional
    void addLink_relationExists_nothingHappened() {
        //given
        jpaSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);

        //when
        jpaSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);
        long countLink = jpaLinkRepository.countByUrl(TEST_URL);
        long countChat = jpaChatRepository.countById(TEST_CHAT_DTO.getId());

        //then
        assertThat(countLink).isEqualTo(1);
        assertThat(countChat).isEqualTo(1);
    }

    @Test
    @Transactional
    void removeLink_relationExists_linkRemoved() {
        //given
        jpaSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);

        //when
        jpaSubscriptionService.removeLink(TEST_CHAT_DTO.getId(), TEST_URL);
        long countLink = jpaLinkRepository.countByUrl(TEST_URL);
        long countChat = jpaChatRepository.countById(TEST_CHAT_DTO.getId());

        //then
        assertThat(countLink).isEqualTo(0);
        assertThat(countChat).isEqualTo(0);
    }

    @Test
    @Transactional
    void listAll_chatTracksSomeLinks_listOfLinksReturned() {
        //given
        jpaSubscriptionService.addLink(ENITY_TEST_CHAT.getId(), ENTITY_TEST_LINK.getUrl());
        jpaSubscriptionService.addLink(ENITY_TEST_CHAT.getId(), ENTITY_TEST_LINK_2.getUrl());

        //when
        final List<LinkDto> linkDtos = jpaSubscriptionService.listAll(TEST_CHAT_DTO.getId());
        List<String> expectedLinks = List.of(TEST_URL, TEST_URL_2);

        //then
        assertThat(linkDtos.size()).isEqualTo(2);
        assertTrue(expectedLinks.contains(linkDtos.get(0).getUrl()));
        assertTrue(expectedLinks.contains(linkDtos.get(1).getUrl()));
    }

    @Test
    @Transactional
    void listAll_chatDoesNotTrackLinks_exceptionThrown() {
        assertThat(jpaSubscriptionService.listAll(TEST_CHAT_DTO.getId()).size()).isEqualTo(0);
    }

    @Test
    @Transactional
    void chatList_relationExists_listReturned() {
        //given
        jpaSubscriptionService.addLink(ENITY_TEST_CHAT.getId(), ENTITY_TEST_LINK.getUrl());
        jpaSubscriptionService.addLink(ENITY_TEST_CHAT.getId(), ENTITY_TEST_LINK_2.getUrl());

        //when
        final List<ChatDto> chatDtos = jpaSubscriptionService.chatList(TEST_URL);

        //then
        assertThat(chatDtos.size()).isEqualTo(1);
        assertThat(chatDtos.get(0)).isEqualTo(TEST_CHAT_DTO);
    }

    @Test
    @Transactional
    void chatList_noRelation_exceptionThrown() {
        assertThat(jpaSubscriptionService.chatList(TEST_URL).size()).isEqualTo(0);
    }
}
