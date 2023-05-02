package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationProperties;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.persistence.service.rabbitmq.ScrapperQueueProducer;

import java.util.List;

@EnableScheduling
@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
@EnableConfigurationProperties(ApplicationProperties.class)
public class    ScrapperApplication {

        public static void main(String[] args) {
                var ctx = SpringApplication.run(ScrapperApplication.class, args);
                ApplicationConfiguration config = ctx.getBean(ApplicationConfiguration.class);
                System.out.println(config);
                ScrapperQueueProducer producer = ctx.getBean(ScrapperQueueProducer.class);
                producer.send(new LinkUpdateRequest(1, "https://github.com/vbandurin7", "myUri", List.of(1L)));
        }
}