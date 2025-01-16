package br.com.helpdesk.config;

import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitMqConfig {

    @Bean
    public SimpleMessageConverter simpleMessageConverter() {

        SimpleMessageConverter messageConverter = new SimpleMessageConverter();
        messageConverter.setAllowedListPatterns(List.of(
                "models.*",
                "java.util.*",
                "java.time.*",
                "java.lang.*"
        ));

        return messageConverter;
    }
}
