package ru.tinkoff.edu.scrapper;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

public class JdbcRepositoryTestEnvironment extends IntegrationEnvironment {

    @ComponentScan("ru.tinkoff.edu.java.scrapper.persistence")
    @Configuration
    static class JdbcLinkServiceTestConfiguration {}
}
