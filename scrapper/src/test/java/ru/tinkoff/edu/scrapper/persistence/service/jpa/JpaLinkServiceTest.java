package ru.tinkoff.edu.scrapper.persistence.service.jpa;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaLinkService;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.*;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
public class JpaLinkServiceTest {

    private static final Chat enityTestChat = new Chat(1L, new HashSet<>());

    private static final ru.tinkoff.edu.java.scrapper.persistence.entity.Link entityTestLink
            = new ru.tinkoff.edu.java.scrapper.persistence.entity.Link(TEST_URL, new HashSet<>(),
            LINK_INFO, OffsetDateTime.now(), OffsetDateTime.now());

    static {
        enityTestChat.getLinks().add(entityTestLink);
        entityTestLink.getChats().add(enityTestChat);
    }

    @Autowired
    private JpaLinkService jpaLinkService;

    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Autowired
    private JpaChatRepository jpaChatRepository;

    @BeforeEach
    public void clearDB() {
        jpaLinkRepository.deleteAll();
    }

    @Test
    void save_linkDoesNotExist_linkAddedSuccessfully() {
        //given
        LinkSaveRequest lr = new LinkSaveRequest(new LinkDto(TEST_URL), entityTestLink);

        //when
        jpaChatRepository.save(enityTestChat);
        jpaLinkService.save(lr);
        long count = jpaLinkRepository.countByUrl(TEST_URL);
        var testLink = jpaLinkRepository.findByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(1);
        assertThat(testLink.getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void save_linkExist_linkWasNotAdded() {
        //given
        LinkSaveRequest lr = new LinkSaveRequest(new LinkDto(TEST_URL), entityTestLink);
        jpaChatRepository.save(enityTestChat);
        jpaLinkRepository.save(lr.getEntityLink());
        long expectedCount = jpaLinkRepository.countByUrl(TEST_URL);

        jpaLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), entityTestLink));
        long count = jpaLinkService.count(TEST_URL);
        var entityTestUrl = jpaLinkRepository.findByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount).isEqualTo(1);
        assertThat(entityTestUrl.getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void delete_linkExists_linkIsDeleted() {
        //given
        jpaLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), entityTestLink));
        Long expectedCount = jpaLinkRepository.countByUrl(TEST_URL);

        //when
        jpaLinkService.delete(TEST_URL);
        Long count = jpaLinkRepository.countByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount - 1).isEqualTo(0);
    }

    @Test
    void delete_linkDoesNotExists_nothingHappened() {
        //given
        Long expectedCount = jpaLinkRepository.countByUrl(TEST_URL);

        //when
        jpaLinkService.delete(TEST_URL);
        Long count = jpaLinkRepository.countByUrl(TEST_URL);

        //then
        assertThat(count).isEqualTo(expectedCount).isEqualTo(0);
    }

    @Test
    void findByUrl_linkExists_linkFound() {
        //given
        jpaLinkRepository.save(entityTestLink);

        //when
        LinkDto byUrl = jpaLinkService.findByUrl(TEST_URL);

        //then
        assertThat(byUrl.getUrl()).isEqualTo(TEST_URL);
    }

    @Test
    void findByUrl_linkDoesNotExist_entityTestLinkReturned() {
        assertThat(jpaLinkService.findByUrl(TEST_URL)).isEqualTo(null);
    }

    @Test
    void count_linkExists_returnOne() {
        //given
        jpaLinkService.save(new LinkSaveRequest(new LinkDto(TEST_URL), entityTestLink));

        //when
        long count = jpaLinkService.count(TEST_URL);

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void count_linkNotExist_returnZero() {
        assertThat(jpaLinkService.count(TEST_URL)).isEqualTo(0);
    }

    @Test
    void updateTime_linkNotExist_nothingHappens() {

        //when
        jpaLinkService.updateTime(new LinkDto(TEST_URL));

        //then
        assertThat(jpaLinkService.findByUrl(TEST_URL)).isEqualTo(null);
    }

    @Test
    @SneakyThrows
    void updateTime_linkExists_timeIsUpdated() {
        //given
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckedAt = OffsetDateTime.now();
        entityTestLink.setUpdatedAt(updatedAt);
        entityTestLink.setLastCheckedAt(lastCheckedAt);
        jpaLinkRepository.save(entityTestLink);


        //when
        OffsetDateTime updatedAt2 = OffsetDateTime.now().plus(1, ChronoUnit.MINUTES);
        OffsetDateTime lastCheckedAt2 = OffsetDateTime.now().plus(1, ChronoUnit.MINUTES);
        var testLink = new LinkDto(entityTestLink.getId(), TEST_URL, LINK_INFO, updatedAt2, lastCheckedAt2);
        jpaLinkService.updateTime(testLink);
        testLink = jpaLinkService.findByUrl(TEST_URL);

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
        entityTestLink.setUpdatedAt(updatedAt);
        entityTestLink.setLastCheckedAt(lastCheckedAt);
        jpaLinkRepository.save(entityTestLink);

        //when
        final List<LinkDto> unchecked = jpaLinkService.findUnchecked();

        //then
        assertThat(unchecked.size()).isEqualTo(0);
    }

    @Test
    void findUnchecked_oneOldLink_listWithOldLink() {
        //given
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckedAt = OffsetDateTime.now().minus(6, ChronoUnit.MINUTES);
        entityTestLink.setUpdatedAt(updatedAt);
        entityTestLink.setLastCheckedAt(lastCheckedAt);
        jpaLinkRepository.save(entityTestLink);

        //when
        final List<LinkDto> unchecked = jpaLinkService.findUnchecked();

        //then
        assertThat(unchecked.size()).isEqualTo(1);
    }
}
