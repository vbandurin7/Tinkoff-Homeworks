package ru.tinkoff.edu.java.scrapper.persistence.service.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationProperties;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class ScrapperQueueProducer implements UpdateSender {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;

    public void postUpdate(LinkUpdateRequest update) {
        rabbitTemplate.convertAndSend(applicationProperties.exchangeName(), applicationProperties.queueName(), update);
    }
}