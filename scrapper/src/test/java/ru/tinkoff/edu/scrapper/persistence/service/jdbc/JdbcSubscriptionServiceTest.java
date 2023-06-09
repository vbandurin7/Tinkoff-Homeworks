package ru.tinkoff.edu.scrapper.persistence.service.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcSubscriptionService;
import ru.tinkoff.edu.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.*;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@ActiveProfiles("test")
class JdbcSubscriptionServiceTest extends IntegrationEnvironment {

    @Autowired
    private JdbcSubscriptionService jdbcSubscriptionService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcLinkService jdbcLinkService;

    @BeforeEach
    public void clearDB() {
        jdbcTemplate.update(CLEAR_CHAT_LINK_SQL);
        jdbcTemplate.update(CLEAR_LINK_SQL);
        jdbcTemplate.update(CLEAR_CHAT_SQL);
    }

    @Test
    void addLink_relationNotExist_relationAdded() {
        //given
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT_DTO.getId());

        //when
        jdbcSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);
        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_LINK_SQL, Long.class, TEST_URL, TEST_CHAT_DTO.getId());

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void addLink_relationExists_nothingHappened() {
        //given
        jdbcLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT_DTO.getId());
        jdbcTemplate.update(INSERT_CHAT_LINK_SQL, TEST_CHAT_DTO.getId(), TEST_URL);

        //when
        jdbcSubscriptionService.addLink(TEST_CHAT_DTO.getId(), TEST_URL);
        Long count = jdbcTemplate.queryForObject(
                COUNT_CHAT_LINK_SQL, Long.class, TEST_URL, TEST_CHAT_DTO.getId());

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void removeLink_relationExists_linkRemoved() {
        //given
        jdbcLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT_DTO.getId());
        jdbcTemplate.update("INSERT INTO chat_link VALUES (?,?)", TEST_CHAT_DTO.getId(), TEST_URL);

        //when
        jdbcSubscriptionService.removeLink(TEST_CHAT_DTO.getId(), TEST_URL);
        Long count = jdbcTemplate.queryForObject(
                COUNT_CHAT_LINK_SQL, Long.class, TEST_URL, TEST_CHAT_DTO.getId());

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    void removeLink_relationNotExist_nothingHappened() {
        //given
        jdbcLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT_DTO.getId());

        //when
        jdbcSubscriptionService.removeLink(TEST_CHAT_DTO.getId(), TEST_URL);
        Long count = jdbcTemplate.queryForObject(
                COUNT_CHAT_LINK_SQL, Long.class, TEST_URL, TEST_CHAT_DTO.getId());

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    void listAll_chatTracksSomeLinks_listOfLinksReturned() {
        //given
        jdbcLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));
        jdbcLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL_2), null));
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT_DTO.getId());
        jdbcTemplate.update(INSERT_CHAT_LINK_SQL, TEST_CHAT_DTO.getId(), TEST_URL_2);
        jdbcTemplate.update(INSERT_CHAT_LINK_SQL, TEST_CHAT_DTO.getId(), TEST_URL);

        //when
        final List<LinkDto> linkDtos = jdbcSubscriptionService.listAll(TEST_CHAT_DTO.getId());
        List<String> expectedLinks = List.of(TEST_URL, TEST_URL_2);

        //then
        assertThat(linkDtos.size()).isEqualTo(2);
        assertTrue(expectedLinks.contains(linkDtos.get(0).getUrl()));
        assertTrue(expectedLinks.contains(linkDtos.get(1).getUrl()));
    }

    @Test
    void listAll_chatDoesNotTrackLinks_noRelations() {
        //when
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT_DTO.getId());
        final List<LinkDto> linkDtos = jdbcSubscriptionService.listAll(TEST_CHAT_DTO.getId());

        //then
        assertThat(linkDtos.size()).isEqualTo(0);
    }

    @Test
    void chatList_relationExists_listReturned() {
        //given
        jdbcLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));
        jdbcTemplate.update(INSERT_CHAT_SQL, TEST_CHAT_DTO.getId());
        jdbcTemplate.update(INSERT_CHAT_LINK_SQL, TEST_CHAT_DTO.getId(), TEST_URL);

        //when
        final List<ChatDto> chatDtos = jdbcSubscriptionService.chatList(TEST_URL);

        //then
        assertThat(chatDtos.size()).isEqualTo(1);
        assertThat(chatDtos.get(0)).isEqualTo(TEST_CHAT_DTO);
    }

    @Test
    void chatList_noRelation_emptyListReturned() {
        assertThat(jdbcSubscriptionService.chatList(TEST_URL).size()).isEqualTo(0);
    }
}
