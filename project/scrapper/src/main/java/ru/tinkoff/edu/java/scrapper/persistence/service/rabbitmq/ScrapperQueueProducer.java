package ru.tinkoff.edu.java.scrapper.persistence.service.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationProperties;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;

    public void send(LinkUpdateRequest update) {
        rabbitTemplate.convertAndSend(applicationProperties.exchangeName(), applicationProperties.queueName(), update);
    }
}