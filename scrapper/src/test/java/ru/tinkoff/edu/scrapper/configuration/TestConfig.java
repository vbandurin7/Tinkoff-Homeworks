package ru.tinkoff.edu.scrapper.configuration;

import lombok.SneakyThrows;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import ru.tinkoff.edu.scrapper.IntegrationEnvironment;

import javax.sql.DataSource;

@TestConfiguration
@Import(ServiceConfiguration.class)
public class TestConfig extends IntegrationEnvironment {

    @SneakyThrows
    @Bean
    public DataSource dataSource() {
        return new SingleConnectionDataSource(POSTGRE_SQL_CONTAINER.createConnection(""), true);
    }
}
