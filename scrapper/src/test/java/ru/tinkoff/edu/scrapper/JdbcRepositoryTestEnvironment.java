package ru.tinkoff.edu.scrapper;

import org.jooq.impl.QOM;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public class JdbcRepositoryTestEnvironment extends IntegrationEnvironment {

    @ComponentScan("ru.tinkoff.edu.java.scrapper.persistence")
    @Configuration
    static class JdbcLinkServiceTestConfiguration {}
}
