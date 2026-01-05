package com.example.argusclone.events.eventlisteners;

import com.example.argusclone.config.RabbitMqConfig;
import com.example.argusclone.events.eventclasses.StudentDeletionEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.logging.Logger;

@Component
public class StudentDeletionEventListener {
    private static final Logger log = Logger.getLogger(StudentDeletionEventListener.class.getName());

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public StudentDeletionEventListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteStudentInResumeService(StudentDeletionEvent event) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.DELETE_ROUTING_KEY, event.email());
        log.info("Student successfully deleted in resume microservice: {}");
    }
}
