package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.entities.Group;
import com.example.argusclone.entities.Student;
import com.example.argusclone.events.eventclasses.StudentCreationEvent;
import com.example.argusclone.events.eventclasses.StudentDeletionEvent;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.StudentAdditionDeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentAdditionDeletionServiceImpl implements StudentAdditionDeletionService {
    private final StudentRepository studentRepository;
    private final StudentCourseResultRepository studentCourseResultRepository;
    private final StudentMapper studentMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CacheManager cacheManager;

    @Autowired
    public StudentAdditionDeletionServiceImpl(StudentRepository studentRepository,
                                              StudentCourseResultRepository studentCourseResultRepository,
                                              StudentMapper studentMapper,
                                              ApplicationEventPublisher applicationEventPublisher,
                                              CacheManager cacheManager) {
        this.studentRepository = studentRepository;
        this.studentCourseResultRepository = studentCourseResultRepository;
        this.studentMapper = studentMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.cacheManager = cacheManager;
    }

    @Override
    @Transactional
    @CachePut(value = "STUDENT_CACHE", key = "'id: ' + #result.id")
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
    @Caching(evict = {
            @CacheEvict(value = "STUDENT_CACHE", key = "'id: ' + #id"),
            @CacheEvict(value = "COURSE_CACHE_LIST", key = "'studentId: ' + #id"),
            @CacheEvict(value = "STUDENT_COURSE_RESULTS_CACHE", key = "'studentId: ' + #id")
    })
    public void deleteStudentById(Integer id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + id + " not found")
        );

        student.getGroups().forEach(group -> group.getStudents().remove(student));

        Cache studentCache = cacheManager.getCache("STUDENT_CACHE_LIST");
        for (Group group : student.getGroups()) {
            if (studentCache != null) {
                studentCache.evict("courseId: " + group.getCourse().getId() + ", groupId: " + group.getId());
            }
        }

        Cache scoreCache = cacheManager.getCache("SCORE_CACHE_LIST");
        student.getGroups().forEach(group -> {
            if (scoreCache != null) {
                scoreCache.evict("studentId: " + id + ", courseId: " + group.getCourse().getId());
            }
        });

        studentCourseResultRepository.deleteByStudentId(id);
        studentRepository.delete(student);

        applicationEventPublisher.publishEvent(
                new StudentDeletionEvent(student.getEmail())
        );
    }
}
