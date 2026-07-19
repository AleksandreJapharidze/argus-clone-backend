package com.example.argusclone.services;

import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.entities.Group;
import com.example.argusclone.entities.Student;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.caching.StudentRelatedCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class StudentAdditionDeletionService {
    private final StudentRepository studentRepository;
    private final StudentCourseResultRepository studentCourseResultRepository;
    private final StudentMapper studentMapper;
    private final StudentRelatedCacheService studentRelatedCacheService;

    @Autowired
    public StudentAdditionDeletionService(StudentRepository studentRepository,
                                          StudentCourseResultRepository studentCourseResultRepository,
                                          StudentMapper studentMapper,
                                          StudentRelatedCacheService studentRelatedCacheService) {
        this.studentRepository = studentRepository;
        this.studentCourseResultRepository = studentCourseResultRepository;
        this.studentMapper = studentMapper;
        this.studentRelatedCacheService = studentRelatedCacheService;
    }

    @CachePut(cacheNames = "student-cache", key = "#result.id()")
    @Transactional
    public StudentResponse createStudent(CreateStudentRequest student) {
        studentRepository.findByEmail(student.email()).ifPresent(s -> {
            throw new DuplicateResourceException("Student with email " + student.email() + " already exists");
        });

        Student studentEntity = studentMapper.toEntity(student);
        Student savedStudent = studentRepository.save(studentEntity);

        return studentMapper.toResponse(savedStudent);
    }

    @CacheEvict(cacheNames = "student-cache", key = "#id")
    @Transactional
    public void deleteStudentById(Integer id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + id + " not found")
        );

        Set<Group> studentGroups = student.getGroups();

        List<Integer> studentGroupIds = studentGroups.stream().map(Group::getId).toList();
        List<Integer> studentCourseIds = studentGroups.stream().map(group -> group.getCourse().getId()).toList();

        studentRelatedCacheService.clearAllRelevantCachesForStudent(id, studentGroupIds, studentCourseIds);

        student.getGroups().forEach(group -> group.getStudents().remove(student));

        studentCourseResultRepository.deleteByStudentId(id);

        studentRepository.delete(student);
    }
}
