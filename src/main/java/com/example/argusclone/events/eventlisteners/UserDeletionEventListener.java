package com.example.argusclone.events.eventlisteners;

import com.example.argusclone.config.RabbitMqConfig;
import com.example.argusclone.events.eventclasses.UserDeletionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserDeletionEventListener {
    private static final Logger log = LoggerFactory.getLogger(UserDeletionEventListener.class.getName());

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserDeletionEventListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteUserInAuthService(UserDeletionEvent event) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.SECOND_DELETE_ROUTING_KEY, event.username());
        log.info("User successfully deleted in auth microservice. User username: {}", event.username());
    }
}
