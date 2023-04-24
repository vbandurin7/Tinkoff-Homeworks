package ru.tinkoff.edu.scrapper.persistence.service;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.scrapper.JdbcRepositoryTestEnvironment;
import ru.tinkoff.edu.scrapper.configuration.TestConfig;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.tinkoff.edu.scrapper.persistence.service.utils.RequestDataProvider.*;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
public class JdbcLinkServiceTest extends JdbcRepositoryTestEnvironment {
    @Autowired
    private JdbcLinkService jdbcLinkService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
}
