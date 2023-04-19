package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfiguration {

    @Bean
    public long schedulerInterval(ApplicationProperties config) {
        return config.scheduler().interval().toMillis();
    }

    @Bean("checkInterval")
    public long checkInterval(ApplicationProperties properties) {
        return properties.checkInterval();
    }
}
