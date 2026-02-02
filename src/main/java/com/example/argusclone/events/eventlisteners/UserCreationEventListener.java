package com.example.argusclone.events.eventlisteners;

import com.example.argusclone.config.RabbitMqConfig;
import com.example.argusclone.events.eventclasses.UserCreationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserCreationEventListener {
    private static final Logger log = LoggerFactory.getLogger(UserCreationEventListener.class.getName());

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserCreationEventListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createUserInAuthService(UserCreationEvent event) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.SECOND_POST_ROUTING_KEY, event);
        log.info("User successfully created in auth microservice: {}", event);
    }
}
