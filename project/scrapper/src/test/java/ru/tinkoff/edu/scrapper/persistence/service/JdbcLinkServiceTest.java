package ru.tinkoff.edu.scrapper.persistence.service;

import lombok.SneakyThrows;
import org.jooq.tools.jdbc.SingleConnectionDataSource;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.scrapper.IntegrationEnvironment;


import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.*;

public class JdbcLinkServiceTest extends IntegrationEnvironment {
    static JdbcLinkService jdbcLinkService;
    static JdbcTemplate jdbcTemplate;

    @SneakyThrows
    @BeforeAll
    public static void init() {
        jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(POSTGRE_SQL_CONTAINER.createConnection("")));
        jdbcLinkService = new JdbcLinkService(new JdbcLinkRepository(jdbcTemplate, 300000), new StackoverflowClient());
    }

    @AfterEach
    public void clearDB() {
        jdbcTemplate.update(CLEAR_LINK_SQL);
    }

    @Test
    void save_linkDoesNotExist_linkAddedSuccessfully() {
        //given
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL, new JSONObject(LINK_INFO).toString());

        //when
        long count = jdbcLinkService.count(TEST_URL);
        Link testUrl = jdbcLinkService.findByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(1);
        assertThat(testUrl.getUrl().toString()).isEqualTo(TEST_URL);
    }

    @Test
    void save_linkExist_linkWasNotAdded() {
        //given
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL, new JSONObject(LINK_INFO).toString());
        long expectedCount = jdbcLinkService.count(TEST_URL);

        //when
        jdbcLinkService.save(new Link(URI.create(TEST_URL)));
        long count = jdbcLinkService.count(TEST_URL);
        Link testUrl = jdbcLinkService.findByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount).isEqualTo(1);
        assertThat(testUrl.getUrl().toString()).isEqualTo(TEST_URL);
    }

    @Test
    void delete_linkExists_linkIsDeleted() {
        //given
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL, new JSONObject(LINK_INFO).toString());
        Long expectedCount = jdbcTemplate.queryForObject(COUNT_LINK_SQL, Long.class, TEST_URL);
        
        //when
        jdbcLinkService.delete(URI.create(TEST_URL));
        Long count = jdbcTemplate.queryForObject(COUNT_LINK_SQL, Long.class, TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount - 1);
    }

    @Test
    void delete_linkDoesNotExists_nothingHappened() {
        //given
        Long expectedCount = jdbcTemplate.queryForObject(COUNT_LINK_SQL, Long.class, TEST_URL);

        //when
        jdbcLinkService.delete(URI.create(TEST_URL));
        Long count = jdbcTemplate.queryForObject(COUNT_LINK_SQL, Long.class, TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount);
    }

    @Test
    void findByUrl_linkExists_linkFound() {
        //given
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL, new JSONObject(LINK_INFO).toString());

        //when
        Link byUrl = jdbcLinkService.findByUrl(TEST_URL);

        //then
        assertThat(byUrl.getUrl().toString()).isEqualTo(TEST_URL);
    }

    @Test
    void findByUrl_linkDoesNotExist_linkFound() {
        assertThat(jdbcLinkService.findByUrl(TEST_URL)).isEqualTo(null);
    }

    @Test
    void count_linkExists_returnOne() {
        //given
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL, new JSONObject(LINK_INFO).toString());

        //when
        long count = jdbcLinkService.count(TEST_URL);

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void count_linkNotExist_returnZero() {
        assertThat(jdbcLinkService.count(TEST_URL)).isEqualTo(0);
    }

    @Test
    void updateTime_linkNotExist_nothingHappens() {

        //when
        jdbcLinkService.updateTime(new Link(URI.create(TEST_URL)));

        //then
        assertThat(jdbcLinkService.findByUrl(TEST_URL)).isEqualTo(null);
    }

    @Test
    void updateTime_linkExists_timeIsUpdated() {
        //given
        Timestamp updatedAt = Timestamp.from(Instant.now());
        Timestamp lastCheckedAt = Timestamp.from(Instant.now());
        Link testLink = new Link(1L, URI.create(TEST_URL), null, lastCheckedAt, updatedAt);
        jdbcLinkService.save(testLink);

        //when
        Timestamp updatedAt2 = Timestamp.from(Instant.now());
        Timestamp lastCheckedAt2 = Timestamp.from(Instant.now());
        testLink = new Link(1L, URI.create(TEST_URL), null, updatedAt2, lastCheckedAt2);
        jdbcLinkService.updateTime(testLink);
        testLink = jdbcLinkService.findByUrl(TEST_URL);

        //then
        assertThat(testLink.getUpdatedAt().getTime() - updatedAt2.getTime()).isEqualTo(0);
        assertThat(testLink.getUpdatedAt().getTime() - updatedAt.getTime()).isNotEqualTo(0);
        assertThat(testLink.getLastCheckedAt().getTime() - lastCheckedAt2.getTime()).isEqualTo(0);
        assertThat(testLink.getLastCheckedAt().getTime() - lastCheckedAt.getTime()).isNotEqualTo(0);
    }
}
