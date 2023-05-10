package ru.tinkoff.edu.scrapper.persistence.service.jdbc;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.scrapper.JdbcRepositoryTestEnvironment;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.*;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@ActiveProfiles("test")
class JdbcLinkServiceTest extends JdbcRepositoryTestEnvironment {

    @Autowired
    private JdbcLinkService jdbcLinkService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void clearDB() {
        jdbcTemplate.update(CLEAR_CHAT_LINK_SQL);
        jdbcTemplate.update(CLEAR_LINK_SQL);
    }

    @Test
    void save_linkDoesNotExist_linkAddedSuccessfully() {
        //given
        LinkSaveRequest lr = new LinkSaveRequest(new LinkDto(TEST_URL), null);

        //when
        jdbcLinkService.save(lr);
        long count = jdbcLinkService.count(TEST_URL);
        LinkDto testUrl = jdbcLinkService.findByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(1);
        assertThat(testUrl.getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void save_linkExist_linkWasNotAdded() {
        //given
        LinkSaveRequest lr = new LinkSaveRequest(new LinkDto(TEST_URL), null);
        jdbcLinkService.save(lr);
        long expectedCount = jdbcLinkService.count(TEST_URL);

        //when
        jdbcLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));
        long count = jdbcLinkService.count(TEST_URL);
        LinkDto testUrl = jdbcLinkService.findByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount).isEqualTo(1);
        assertThat(testUrl.getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void delete_linkExists_linkIsDeleted() {
        //given
        jdbcLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));
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
        jdbcLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));

        //when
        LinkDto byUrl = jdbcLinkService.findByUrl(TEST_URL);

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
        jdbcLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));

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
        jdbcLinkService.updateTime(new LinkDto(TEST_URL));

        //then
        assertThat(jdbcLinkService.findByUrl(TEST_URL)).isEqualTo(null);
    }

    @Test
    @SneakyThrows
    void updateTime_linkExists_timeIsUpdated() {
        //given
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckedAt = OffsetDateTime.now();
        LinkDto testLinkDto = new LinkDto(1L, TEST_URL, null, lastCheckedAt, updatedAt);
        jdbcTemplate.update("INSERT INTO link (url, link_info, last_checked_at, updated_at) VALUES (?, ?::jsonb, ?, ?)",
                TEST_URL, new JSONObject(LINK_INFO).toString(), lastCheckedAt, updatedAt);

        //when
        OffsetDateTime updatedAt2 = OffsetDateTime.now().plus(1, ChronoUnit.MINUTES);
        OffsetDateTime lastCheckedAt2 = OffsetDateTime.now().plus(1, ChronoUnit.MINUTES);
        testLinkDto = new LinkDto(1L, TEST_URL, LINK_INFO, updatedAt2, lastCheckedAt2);
        jdbcLinkService.updateTime(testLinkDto);
        testLinkDto = jdbcLinkService.findByUrl(TEST_URL);

        //then
        assertThat(testLinkDto.getUpdatedAt().toEpochSecond() - updatedAt2.toEpochSecond()).isEqualTo(0);
        assertThat(testLinkDto.getUpdatedAt().toEpochSecond() - updatedAt.toEpochSecond()).isNotEqualTo(0);
        assertThat(testLinkDto.getLastCheckedAt().toEpochSecond() - lastCheckedAt2.toEpochSecond()).isEqualTo(0);
        assertThat(testLinkDto.getLastCheckedAt().toEpochSecond() - lastCheckedAt.toEpochSecond()).isNotEqualTo(0);
    }

    @Test
    void findUnchecked_noLinksToRecheck_emptyListReturned() {
        //given
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckedAt = OffsetDateTime.now();
        jdbcTemplate.update("INSERT INTO link (url, link_info, last_checked_at, updated_at) VALUES (?, ?::jsonb, ?, ?)",
                TEST_URL, new JSONObject(LINK_INFO).toString(), lastCheckedAt, updatedAt);

        //when
        final List<LinkDto> unchecked = jdbcLinkService.findUnchecked();

        //then
        assertThat(unchecked.size()).isEqualTo(0);
    }

    @Test
    void findUnchecked_oneOldLink_listWithOldLink() {
        //given
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckedAt = OffsetDateTime.now().minus(6, ChronoUnit.MINUTES);
        LinkDto testLinkDto = new LinkDto(TEST_URL);
        jdbcTemplate.update("INSERT INTO link (url, link_info, last_checked_at, updated_at) VALUES (?, ?::jsonb, ?, ?)",
                TEST_URL, new JSONObject(LINK_INFO).toString(), lastCheckedAt, updatedAt);
        //when
        final List<LinkDto> unchecked = jdbcLinkService.findUnchecked();

        //then
        assertThat(unchecked.size()).isEqualTo(1);
    }
}
