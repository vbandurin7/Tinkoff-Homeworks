package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.dto.request.LinkUpdateRequest;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    public Queue scrapperQueue(ApplicationProperties applicationProperties) {
        return QueueBuilder.durable(applicationProperties.queueName())
                .withArgument("x-dead-letter-exchange", applicationProperties.queueName().concat(".dlx"))
                .build();
    }
    @Bean
    public DirectExchange scrapperExchange(ApplicationProperties applicationProperties) {
        return new DirectExchange(applicationProperties.exchangeName(), true, false);
    }

    @Bean
    public Binding binding(Queue scrapperQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(scrapperQueue).to(directExchange).withQueueName();
    }

    @Bean
    public FanoutExchange deadLetterExchange(ApplicationProperties applicationProperties) {
        return new FanoutExchange(applicationProperties.queueName().concat(".dlx"));
    }

    @Bean
    public Queue deadLetterQueue(ApplicationProperties applicationProperties) {
        return QueueBuilder.durable(applicationProperties.queueName().concat(".dlq")).build();
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange);
    }

    @Bean
    public ClassMapper classMapper(){
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest", LinkUpdateRequest.class);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("ru.tinkoff.edu.java.scrapper.dto.request.*");
        classMapper.setIdClassMapping(mappings);
        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ClassMapper classMapper){
        Jackson2JsonMessageConverter jsonConverter=new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }
}
