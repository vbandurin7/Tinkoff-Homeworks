package ru.tinkoff.edu.scrapper.configuration;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import ru.tinkoff.edu.scrapper.IntegrationEnvironment;

import javax.sql.DataSource;

@Configuration
public class TestConfig extends IntegrationEnvironment {
    @SneakyThrows
    @Bean
    public DataSource dataSource() {
        return new SingleConnectionDataSource(POSTGRE_SQL_CONTAINER.createConnection(""), true);
    }
}
