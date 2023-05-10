package ru.tinkoff.edu.scrapper.persistence.service.jpa;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaChatService;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class JpaChatServiceTest {

    private static final Chat ENITY_TEST_CHAT = new Chat(1L, new HashSet<>());

    private static final ChatSaveRequest CHAT_SAVE_REQUEST = new ChatSaveRequest(
            new ChatDto(1L), ENITY_TEST_CHAT);
    @Autowired
    private JpaChatService jpaChatService;

    @Autowired
    private JpaChatRepository jpaChatRepository;

    @AfterEach
    public void clearDB() {
        jpaChatRepository.deleteAll();
    }

    @Test
    void register_chatNotExists_chatRegistered() {
        assertThat(jpaChatService.register(CHAT_SAVE_REQUEST)).isEqualTo(CHAT_SAVE_REQUEST.getDtoChat());
    }

    @Test
    void register_chatExists_chatReturned() {
        assertThat(jpaChatService.register(CHAT_SAVE_REQUEST)).isEqualTo(CHAT_SAVE_REQUEST.getDtoChat());
    }

    @Test
    void unregister_chatExists_chatDeleted() {
        //given
        jpaChatRepository.save(CHAT_SAVE_REQUEST.getEntityChat());

        //when
        jpaChatService.unregister(CHAT_SAVE_REQUEST.getDtoChat().getId());
        Long count = jpaChatRepository.countById(CHAT_SAVE_REQUEST.getDtoChat().getId());

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    void unregister_chatNotExists_nothingHappened() {
        //when
        jpaChatService.unregister(CHAT_SAVE_REQUEST.getDtoChat().getId());
        Long count = jpaChatRepository.countById(CHAT_SAVE_REQUEST.getDtoChat().getId());

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    void count_chatExists_returnOne() {
        //given
        jpaChatRepository.save(CHAT_SAVE_REQUEST.getEntityChat());

        //when
        long count = jpaChatService.count(CHAT_SAVE_REQUEST.getDtoChat().getId());

        //then
        assertThat(count).isEqualTo(1);
    }
}
