package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationProperties;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class ScrapperApplication {
        public static void main(String[] args) {
                var ctx = SpringApplication.run(ScrapperApplication.class, args);
                ApplicationConfiguration config = ctx.getBean(ApplicationConfiguration.class);
                System.out.println(config);

        }
}