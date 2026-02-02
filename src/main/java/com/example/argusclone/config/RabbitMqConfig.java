package com.example.argusclone.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String EXCHANGE = "post_exchange";

    public static final String POST_QUEUE = "post_queue";
    public static final String SECOND_POST_QUEUE = "second_post_queue";
    public static final String DELETE_QUEUE = "delete_queue";
    public static final String SECOND_DELETE_QUEUE = "second_delete_queue";

    public static final String POST_ROUTING_KEY = "post_routing_key";
    public static final String SECOND_POST_ROUTING_KEY = "second_post_routing_key";

    public static final String DELETE_ROUTING_KEY = "delete_routing_key";
    public static final String SECOND_DELETE_ROUTING_KEY = "second_delete_routing_key";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue postQueue() {
        return QueueBuilder.durable(POST_QUEUE).build();
    }

    @Bean
    public Queue secondPostQueue() {
        return QueueBuilder.durable(SECOND_POST_QUEUE).build();
    }

    @Bean
    public Queue deleteQueue() {
        return QueueBuilder.durable(DELETE_QUEUE).build();
    }

    @Bean
    public Queue secondDeleteQueue() {
        return QueueBuilder.durable(SECOND_DELETE_QUEUE).build();
    }

    @Bean
    public Binding postBinding() {
        return BindingBuilder
                .bind(postQueue())
                .to(directExchange())
                .with(POST_ROUTING_KEY);
    }

    @Bean
    public Binding secondPostBinding() {
        return BindingBuilder
                .bind(secondPostQueue())
                .to(directExchange())
                .with(SECOND_POST_ROUTING_KEY);
    }

    @Bean
    public Binding deleteBinding() {
        return BindingBuilder
                .bind(deleteQueue())
                .to(directExchange())
                .with(DELETE_ROUTING_KEY);
    }

    @Bean
    public Binding secondDeleteBinding() {
        return BindingBuilder
                .bind(secondDeleteQueue())
                .to(directExchange())
                .with(SECOND_DELETE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
