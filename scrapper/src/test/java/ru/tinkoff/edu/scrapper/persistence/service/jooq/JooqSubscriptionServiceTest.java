package ru.tinkoff.edu.scrapper.persistence.service.jooq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jooq.JooqSubscriptionService;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.TEST_CHAT_DTO;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.TEST_URL;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.TEST_URL_2;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@ActiveProfiles("test")
public class JooqSubscriptionServiceTest {

    @Autowired
    private JooqLinkRepository jooqLinkRepository;
    @Autowired
    private JooqSubscriptionRepository jooqSubscriptionRepository;
    @Autowired
    private JooqChatRepository jooqChatRepository;
    @Autowired
    private JooqSubscriptionService jooqSubscriptionService;

    @BeforeEach
    public void prepDB() {
        jooqSubscriptionRepository.deleteAll();
        jooqLinkRepository.deleteAll();
        jooqChatRepository.deleteAll();
    }

    @Test
    void addLink_relationNotExist_relationAdded() {
        //given
        jooqChatRepository.save(TEST_CHAT_DTO);

        //when
        jooqSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);
        final List<LinkDto> allByChat = jooqSubscriptionRepository.findAllByChat(TEST_CHAT_DTO.getId());

        //then
        assertThat(allByChat.size()).isEqualTo(1);
        assertThat(allByChat.get(0).getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void addLink_relationExists_nothingHappened() {
        //given
        jooqLinkRepository.save(new LinkDto(TEST_URL));
        jooqChatRepository.save(TEST_CHAT_DTO);
        jooqSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);

        //when
        jooqSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);
        final List<LinkDto> allByChat = jooqSubscriptionRepository.findAllByChat(TEST_CHAT_DTO.getId());

        //then
        assertThat(allByChat.size()).isEqualTo(1);
        assertThat(allByChat.get(0).getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void removeLink_relationExists_linkRemoved() {
        //given
        jooqLinkRepository.save(new LinkDto(TEST_URL));
        jooqChatRepository.save(TEST_CHAT_DTO);
        jooqSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);

        //when
        jooqSubscriptionService.removeLink(TEST_CHAT_DTO.getId(), TEST_URL);
        final List<LinkDto> allByChat = jooqSubscriptionRepository.findAllByChat(TEST_CHAT_DTO.getId());

        //then
        assertThat(allByChat.isEmpty()).isTrue();
    }

    @Test
    void removeLink_relationNotExist_nothingHappened() {
        //given
        jooqLinkRepository.save(new LinkDto(TEST_URL));
        jooqChatRepository.save(TEST_CHAT_DTO);

        //when
        jooqSubscriptionService.removeLink(TEST_CHAT_DTO.getId(), TEST_URL);
        final List<LinkDto> allByChat = jooqSubscriptionRepository.findAllByChat(TEST_CHAT_DTO.getId());

        //then
        assertThat(allByChat.isEmpty()).isTrue();
    }

    @Test
    void listAll_chatTracksSomeLinks_listOfLinksReturned() {
        //given
        jooqLinkRepository.save(new LinkDto(TEST_URL));
        jooqLinkRepository.save(new LinkDto(TEST_URL_2));
        jooqChatRepository.save(TEST_CHAT_DTO);
        jooqSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);
        jooqSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL_2);

        //when
        final List<LinkDto> linkDtos = jooqSubscriptionService.listAll(TEST_CHAT_DTO.getId());
        List<String> expectedLinks = List.of(TEST_URL, TEST_URL_2);

        //then
        assertThat(linkDtos.size()).isEqualTo(2);
        assertTrue(expectedLinks.contains(linkDtos.get(0).getUrl()));
        assertTrue(expectedLinks.contains(linkDtos.get(1).getUrl()));
    }

    @Test
    void listAll_chatDoesNotTrackLinks_noRelations() {
        //when
        jooqChatRepository.save(TEST_CHAT_DTO);
        final List<LinkDto> linkDtos = jooqSubscriptionService.listAll(TEST_CHAT_DTO.getId());

        //then
        assertThat(linkDtos.size()).isEqualTo(0);
    }

    @Test
    void chatList_relationExists_listReturned() {
        //given
        jooqLinkRepository.save(new LinkDto(TEST_URL));
        jooqChatRepository.save(TEST_CHAT_DTO);
        jooqSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);

        //when
        final List<ChatDto> chatDtos = jooqSubscriptionService.chatList(TEST_URL);

        //then
        assertThat(chatDtos.size()).isEqualTo(1);
        assertThat(chatDtos.get(0)).isEqualTo(TEST_CHAT_DTO);
    }

    @Test
    void chatList_noRelation_emptyListReturned() {
        assertThat(jooqSubscriptionService.chatList(TEST_URL).size()).isEqualTo(0);
    }
}
