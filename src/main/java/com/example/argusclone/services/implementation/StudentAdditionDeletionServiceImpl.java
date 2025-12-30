package com.example.argusclone.services.implementation;

import com.example.argusclone.config.RabbitMqConfig;
import com.example.argusclone.dtos.resumeservice.StudentRequestForResumeService;
import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.entities.Student;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.ScoreRepository;
import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.StudentAdditionDeletionService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class StudentAdditionDeletionServiceImpl implements StudentAdditionDeletionService {
    private static final Logger log = Logger.getLogger(StudentAdditionDeletionServiceImpl.class.getName());

    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    private final StudentCourseResultRepository studentCourseResultRepository;
    private final StudentMapper studentMapper;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public StudentAdditionDeletionServiceImpl(StudentRepository studentRepository,
                                      ScoreRepository scoreRepository,
                                      StudentCourseResultRepository studentCourseResultRepository,
                                      StudentMapper studentMapper,
                                      RabbitTemplate rabbitTemplate) {
        this.studentRepository = studentRepository;
        this.scoreRepository = scoreRepository;
        this.studentCourseResultRepository = studentCourseResultRepository;
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

        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.POST_ROUTING_KEY, requestForResumeService);
        log.info("Adding student to resumes microservice: {}");
    }

    @Override
    public void deleteStudentById(Integer id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + id + " not found")
        );

        deleteStudentForResumeServiceByEmail(student);

        student.getGroups().forEach(group -> group.getStudents().remove(student));
//        student.getScores().forEach(score -> score.setStudent(null));
//        student.getStudentCourseResults().forEach(studentCourseResult -> studentCourseResult.setStudent(null));
//        student.getStudentCourseResults().forEach(studentCourseResult -> studentCourseResult.setCourse(null));

        scoreRepository.deleteAll(student.getScores());
        studentCourseResultRepository.deleteAll(student.getStudentCourseResults());
        studentRepository.deleteById(id);
    }

    private void deleteStudentForResumeServiceByEmail(Student student) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.DELETE_ROUTING_KEY, student.getEmail());
        log.info("Deleting student from resumes microservice: {}");
    }
}
