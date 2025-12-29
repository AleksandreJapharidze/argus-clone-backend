package com.example.argusclone.services.implementation;

import com.example.argusclone.config.RabbitMqConfig;
import com.example.argusclone.dtos.resumeservice.StudentRequestForResumeService;
import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.StudentAdditionService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class StudentAdditionServiceImpl implements StudentAdditionService {
    private static final Logger log = Logger.getLogger(StudentAdditionServiceImpl.class.getName());

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public StudentAdditionServiceImpl(StudentRepository studentRepository,
                                      StudentMapper studentMapper,
                                      RabbitTemplate rabbitTemplate) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public StudentResponse createStudent(CreateStudentRequest student) {
        studentRepository.findByEmail(student.getEmail()).ifPresent(s -> {
            throw new DuplicateResourceException("Student with email " + student.getEmail() + " already exists");
        });

        createStudentForResumeService(student);
        return studentMapper.toResponse(studentRepository.save(studentMapper.toEntity(student)));
    }

    private void createStudentForResumeService(CreateStudentRequest student) {
        StudentRequestForResumeService requestForResumeService = new StudentRequestForResumeService(
                student.getName(), student.getEmail()
        );

        rabbitTemplate.convertAndSend(RabbitMqConfig.POST_EXCHANGE, RabbitMqConfig.POST_ROUTING_KEY, requestForResumeService);
        log.info("Adding student to resumes microservice: {}");
    }
}
