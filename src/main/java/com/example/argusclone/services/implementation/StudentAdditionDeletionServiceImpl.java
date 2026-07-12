package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.entities.Student;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.StudentAdditionDeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentAdditionDeletionServiceImpl implements StudentAdditionDeletionService {
    private final StudentRepository studentRepository;
    private final StudentCourseResultRepository studentCourseResultRepository;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentAdditionDeletionServiceImpl(StudentRepository studentRepository,
                                              StudentCourseResultRepository studentCourseResultRepository,
                                              StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentCourseResultRepository = studentCourseResultRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    @Transactional
    public StudentResponse createStudent(CreateStudentRequest student) {
        studentRepository.findByEmail(student.email()).ifPresent(s -> {
            throw new DuplicateResourceException("Student with email " + student.email() + " already exists");
        });

        Student studentEntity = studentMapper.toEntity(student);
        Student savedStudent = studentRepository.save(studentEntity);

        return studentMapper.toResponse(savedStudent);
    }

    @Override
    @Transactional
    public void deleteStudentById(Integer id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + id + " not found")
        );

        student.getGroups().forEach(group -> group.getStudents().remove(student));

        studentCourseResultRepository.deleteByStudentId(id);

        studentRepository.delete(student);
    }
}
