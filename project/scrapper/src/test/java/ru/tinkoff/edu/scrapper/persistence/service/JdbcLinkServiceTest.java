package ru.tinkoff.edu.scrapper.persistence.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepositoryImpl;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.scrapper.IntegrationEnvironment;


import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestProvider.*;

public class JdbcLinkServiceTest extends IntegrationEnvironment {

    private static final String TEST_URL = "testUrl";
    static JdbcLinkService jdbcLinkService;
    static JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void init() {
        jdbcTemplate = new JdbcTemplate(DataSourceBuilder.create()
                .url(POSTGRE_SQL_CONTAINER.getJdbcUrl())
                .username(POSTGRE_SQL_CONTAINER.getUsername())
                .password(POSTGRE_SQL_CONTAINER.getPassword())
                .build());
        jdbcLinkService = new JdbcLinkService(new LinkRepositoryImpl(jdbcTemplate));
    }

    @AfterEach
    public void clearDB() {
        jdbcTemplate.update(CLEAR_LINK_SQL);
    }

    @Test
    void save_linkDoesNotExist_linkAddedSuccessfully() {
        //given
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL);

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
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL);
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
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL);
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
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL);

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
        jdbcTemplate.update(INSERT_LINK_SQL, TEST_URL);

        //when
        long count = jdbcLinkService.count(TEST_URL);

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void count_linkNotExist_returnZero() {
        assertThat(jdbcLinkService.count(TEST_URL)).isEqualTo(0);
    }
}
