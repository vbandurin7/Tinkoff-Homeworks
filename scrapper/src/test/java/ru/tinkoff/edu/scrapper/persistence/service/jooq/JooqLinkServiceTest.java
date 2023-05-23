package ru.tinkoff.edu.scrapper.persistence.service.jooq;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jooq.JooqLinkService;
import ru.tinkoff.edu.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.LINK_INFO;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.TEST_URL;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@ActiveProfiles("test")
public class JooqLinkServiceTest extends IntegrationEnvironment {

    @Autowired
    private JooqLinkService jooqLinkService;
    @Autowired
    private JooqSubscriptionRepository jooqSubscriptionRepository;
    @Autowired
    private JooqLinkRepository jooqLinkRepository;

    @BeforeEach
    public void clearDB() {
        jooqSubscriptionRepository.deleteAll();
        jooqLinkRepository.deleteAll();
    }

    @Test
    void save_linkDoesNotExist_linkAddedSuccessfully() {
        //given
        LinkSaveRequest lr = new LinkSaveRequest(new LinkDto(TEST_URL), null);

        //when
        jooqLinkService.save(lr);
        long count = jooqLinkService.count(TEST_URL);
        LinkDto testUrl = jooqLinkService.findByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(1);
        assertThat(testUrl.getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void save_linkExist_linkWasNotAdded() {
        //given
        LinkSaveRequest lr = new LinkSaveRequest(new LinkDto(TEST_URL), null);
        jooqLinkService.save(lr);
        long expectedCount = jooqLinkService.count(TEST_URL);

        //when
        jooqLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));
        long count = jooqLinkService.count(TEST_URL);
        LinkDto testUrl = jooqLinkService.findByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount).isEqualTo(1);
        assertThat(testUrl.getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void delete_linkExists_linkIsDeleted() {
        //given
        jooqLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));
        long expectedCount = jooqLinkService.count(TEST_URL);

        //when
        jooqLinkService.delete(TEST_URL);
        long count = jooqLinkService.count(TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount - 1).isEqualTo(0);
    }

    @Test
    void delete_linkDoesNotExists_nothingHappened() {
        //given
        Long expectedCount = jooqLinkRepository.countByUrl(TEST_URL);

        //when
        jooqLinkService.delete(TEST_URL);
        long count = jooqLinkRepository.countByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount).isEqualTo(0);
    }

    @Test
    void findByUrl_linkExists_linkFound() {
        //given
        jooqLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));

        //when
        LinkDto byUrl = jooqLinkService.findByUrl(TEST_URL);

        //then
        assertThat(byUrl.getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void findByUrl_linkDoesNotExist_nullReturned() {
        assertThat(jooqLinkService.findByUrl(TEST_URL)).isEqualTo(null);
    }

    @Test
    void count_linkExists_returnOne() {
        //given
        jooqLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), null));

        //when
        long count = jooqLinkService.count(TEST_URL);

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void count_linkNotExist_returnZero() {
        assertThat(jooqLinkService.count(TEST_URL)).isEqualTo(0);
    }

    @Test
    void updateTime_linkNotExist_nothingHappens() {
        //given
        final LinkDto linkDto = new LinkDto(TEST_URL);
        linkDto.setUpdatedAt(OffsetDateTime.now());

        //when
        jooqLinkService.updateTime(linkDto);


        //then
        assertThat(jooqLinkService.findByUrl(TEST_URL)).isEqualTo(null);
    }

    @Test
    @SneakyThrows
    void updateTime_linkExists_timeIsUpdated() {
        //given
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckedAt = OffsetDateTime.now();
        LinkDto testLinkDto = new LinkDto(1L, TEST_URL, null, lastCheckedAt, updatedAt);
        jooqLinkRepository.save(testLinkDto);

        //when
        OffsetDateTime updatedAt2 = OffsetDateTime.now().plus(1, ChronoUnit.MINUTES);
        OffsetDateTime lastCheckedAt2 = OffsetDateTime.now().plus(1, ChronoUnit.MINUTES);
        testLinkDto = new LinkDto(1L, TEST_URL, LINK_INFO, updatedAt2, lastCheckedAt2);
        jooqLinkService.updateTime(testLinkDto);
        testLinkDto = jooqLinkService.findByUrl(TEST_URL);

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
        LinkDto testLinkDto = new LinkDto(1L, TEST_URL, null, lastCheckedAt, updatedAt);
        jooqLinkRepository.save(testLinkDto);

        //when
        final List<LinkDto> unchecked = jooqLinkService.findUnchecked();

        //then
        assertThat(unchecked.size()).isEqualTo(0);
    }

    @Test
    void findUnchecked_oneOldLink_listWithOldLink() {
        //given
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckedAt = OffsetDateTime.now().minus(6, ChronoUnit.MINUTES);
        LinkDto testLinkDto = new LinkDto(1L, TEST_URL, null, lastCheckedAt, updatedAt);
        jooqLinkRepository.save(testLinkDto);

        //when
        final List<LinkDto> unchecked = jooqLinkService.findUnchecked();

        //then
        assertThat(unchecked.size()).isEqualTo(1);
    }
}
