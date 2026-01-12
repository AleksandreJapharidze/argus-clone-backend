package com.example.argusclone.events.eventlisteners;

import com.example.argusclone.config.RabbitMqConfig;
import com.example.argusclone.events.eventclasses.StudentCreationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class StudentCreationEventListener {
    private static final Logger log = LoggerFactory.getLogger(StudentCreationEventListener.class.getName());

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public StudentCreationEventListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createStudentInResumeService(StudentCreationEvent event) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.POST_ROUTING_KEY, event);
        log.info("Student successfully created in resume microservice: {}", event);
    }
}
