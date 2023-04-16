package ru.tinkoff.edu.scrapper.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.scrapper.IntegrationEnvironment;


@JdbcTest
@ExtendWith(SpringExtension.class)

public class JdbcLinkTest extends IntegrationEnvironment {

    @Autowired
    private JdbcLinkService jdbcLinkService;
    private JdbcChatService jdbcChatService;
    private static JdbcTemplate jdbcTemplate;

    @BeforeAll
    void init() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setPortNumbers(new int[] {POSTGRE_SQL_CONTAINER.getFirstMappedPort()});
        dataSource.setServerNames(new String[] {POSTGRE_SQL_CONTAINER.getHost()});
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    void addLink_linkDoesNotExist_linkAddedSuccessfully() {
        //given
    }

    @Test
    void removeTest() {
    }

}
