package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.entities.Student;
import com.example.argusclone.events.eventclasses.StudentCreationEvent;
import com.example.argusclone.events.eventclasses.StudentDeletionEvent;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.ScoreRepository;
import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.StudentAdditionDeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentAdditionDeletionServiceImpl implements StudentAdditionDeletionService {
    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    private final StudentCourseResultRepository studentCourseResultRepository;
    private final StudentMapper studentMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public StudentAdditionDeletionServiceImpl(StudentRepository studentRepository,
                                              ScoreRepository scoreRepository,
                                              StudentCourseResultRepository studentCourseResultRepository,
                                              StudentMapper studentMapper,
                                              ApplicationEventPublisher applicationEventPublisher) {
        this.studentRepository = studentRepository;
        this.scoreRepository = scoreRepository;
        this.studentCourseResultRepository = studentCourseResultRepository;
        this.studentMapper = studentMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public StudentResponse createStudent(CreateStudentRequest student) {
        studentRepository.findByEmail(student.getEmail()).ifPresent(s -> {
            throw new DuplicateResourceException("Student with email " + student.getEmail() + " already exists");
        });

        Student studentEntity = studentMapper.toEntity(student);
        Student savedStudent = studentRepository.save(studentEntity);

        applicationEventPublisher.publishEvent(
                new StudentCreationEvent(savedStudent.getName(), savedStudent.getEmail())
        );

        return studentMapper.toResponse(savedStudent);
    }

    @Override
    @Transactional
    public void deleteStudentById(Integer id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + id + " not found")
        );

        student.getGroups().forEach(group -> group.getStudents().remove(student));

        scoreRepository.deleteByStudentId(id);
        studentCourseResultRepository.deleteByStudentId(id);
        studentRepository.delete(student);

        applicationEventPublisher.publishEvent(
                new StudentDeletionEvent(student.getEmail())
        );
    }
}
