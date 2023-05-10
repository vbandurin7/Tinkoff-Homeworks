package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    public Queue scrapperQueue(ApplicationProperties applicationProperties) {
        return QueueBuilder.durable(applicationProperties.queueName())
                .withArgument("x-dead-letter-exchange", applicationProperties.queueName().concat(".dlx"))
                .build();
    }

    @Bean
    public DirectExchange directExchange(ApplicationProperties applicationProperties) {
        return new DirectExchange(applicationProperties.exchangeName(), true, false);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).withQueueName();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
