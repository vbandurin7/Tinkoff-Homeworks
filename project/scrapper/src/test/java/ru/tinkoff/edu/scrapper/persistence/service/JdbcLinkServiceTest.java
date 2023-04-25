package ru.tinkoff.edu.scrapper.persistence.service;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.scrapper.JdbcRepositoryTestEnvironment;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.*;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
public class JdbcLinkServiceTest extends JdbcRepositoryTestEnvironment {
    @Autowired
    private JdbcLinkService jdbcLinkService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void clearDB() {
        jdbcTemplate.update(CLEAR_LINK_SQL);
    }

    @Test
    void save_linkDoesNotExist_linkAddedSuccessfully() {
        //given
        Link link = new Link(TEST_URL);

        //when
        jdbcLinkService.save(link);
        long count = jdbcLinkService.count(TEST_URL);
        Link testUrl = jdbcLinkService.findByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(1);
        assertThat(testUrl.getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void save_linkExist_linkWasNotAdded() {
        //given
        Link link = new Link(TEST_URL);
        jdbcLinkService.save(link);
        long expectedCount = jdbcLinkService.count(TEST_URL);

        //when
        jdbcLinkService.save(new Link(TEST_URL));
        long count = jdbcLinkService.count(TEST_URL);
        Link testUrl = jdbcLinkService.findByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount).isEqualTo(1);
        assertThat(testUrl.getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void delete_linkExists_linkIsDeleted() {
        //given
        jdbcLinkService.save(new Link(TEST_URL));
        Long expectedCount = jdbcTemplate.queryForObject(COUNT_LINK_SQL, Long.class, TEST_URL);

        //when
        jdbcLinkService.delete(TEST_URL);
        Long count = jdbcTemplate.queryForObject(COUNT_LINK_SQL, Long.class, TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount - 1).isEqualTo(0);
    }

    @Test
    void delete_linkDoesNotExists_nothingHappened() {
        //given
        Long expectedCount = jdbcTemplate.queryForObject(COUNT_LINK_SQL, Long.class, TEST_URL);

        //when
        jdbcLinkService.delete(TEST_URL);
        Long count = jdbcTemplate.queryForObject(COUNT_LINK_SQL, Long.class, TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount).isEqualTo(0);
    }

    @Test
    void findByUrl_linkExists_linkFound() {
        //given
        jdbcLinkService.save(new Link(TEST_URL));

        //when
        Link byUrl = jdbcLinkService.findByUrl(TEST_URL);

        //then
        assertThat(byUrl.getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void findByUrl_linkDoesNotExist_nullReturned() {
        assertThat(jdbcLinkService.findByUrl(TEST_URL)).isEqualTo(null);
    }

    @Test
    void count_linkExists_returnOne() {
        //given
        jdbcLinkService.save(new Link(TEST_URL));

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
        jdbcLinkService.updateTime(new Link(TEST_URL));

        //then
        assertThat(jdbcLinkService.findByUrl(TEST_URL)).isEqualTo(null);
    }

    @Test
    @SneakyThrows
    void updateTime_linkExists_timeIsUpdated() {
        //given
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckedAt = OffsetDateTime.now();
        Link testLink = new Link(1L, TEST_URL, null, lastCheckedAt, updatedAt);
        jdbcTemplate.update("INSERT INTO link (url, link_info, last_checked_at, updated_at) VALUES (?, ?::jsonb, ?, ?)",
                TEST_URL, new JSONObject(LINK_INFO).toString(), lastCheckedAt, updatedAt);

        //when
        OffsetDateTime updatedAt2 = OffsetDateTime.now().plus(1, ChronoUnit.MINUTES);
        OffsetDateTime lastCheckedAt2 = OffsetDateTime.now().plus(1, ChronoUnit.MINUTES);
        testLink = new Link(1L, TEST_URL, LINK_INFO, updatedAt2, lastCheckedAt2);
        jdbcLinkService.updateTime(testLink);
        testLink = jdbcLinkService.findByUrl(TEST_URL);

        //then
        assertThat(testLink.getUpdatedAt().toEpochSecond() - updatedAt2.toEpochSecond()).isEqualTo(0);
        assertThat(testLink.getUpdatedAt().toEpochSecond() - updatedAt.toEpochSecond()).isNotEqualTo(0);
        assertThat(testLink.getLastCheckedAt().toEpochSecond() - lastCheckedAt2.toEpochSecond()).isEqualTo(0);
        assertThat(testLink.getLastCheckedAt().toEpochSecond() - lastCheckedAt.toEpochSecond()).isNotEqualTo(0);
    }
    @Test
    void findUnchecked_noLinksToRecheck_emptyListReturned() {
        //given
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckedAt = OffsetDateTime.now();
        jdbcTemplate.update("INSERT INTO link (url, link_info, last_checked_at, updated_at) VALUES (?, ?::jsonb, ?, ?)",
                TEST_URL, new JSONObject(LINK_INFO).toString(), lastCheckedAt, updatedAt);

        //when
        final List<Link> unchecked = jdbcLinkService.findUnchecked();

        //then
        assertThat(unchecked.size()).isEqualTo(0);
    }

    @Test
    void findUnchecked_oneOldLink_listWithOldLink() {
        //given
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckedAt = OffsetDateTime.now().minus(6, ChronoUnit.MINUTES);
        Link testLink = new Link(TEST_URL);
        jdbcTemplate.update("INSERT INTO link (url, link_info, last_checked_at, updated_at) VALUES (?, ?::jsonb, ?, ?)",
                TEST_URL, new JSONObject(LINK_INFO).toString(), lastCheckedAt, updatedAt);
        //when
        final List<Link> unchecked = jdbcLinkService.findUnchecked();

        //then
        assertThat(unchecked.size()).isEqualTo(1);
    }
}