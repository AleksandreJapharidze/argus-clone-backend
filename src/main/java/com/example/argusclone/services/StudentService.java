package com.example.argusclone.services;

import com.example.argusclone.dtos.result.StudentCourseResultResponse;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.StudentCourseResultMapper;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentCourseResultRepository studentCourseResultRepository;
    private final StudentMapper studentMapper;
    private final StudentCourseResultMapper studentCourseResultMapper;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          StudentCourseResultRepository studentCourseResultRepository,
                          StudentMapper studentMapper,
                          StudentCourseResultMapper studentCourseResultMapper) {
        this.studentRepository = studentRepository;
        this.studentCourseResultRepository = studentCourseResultRepository;
        this.studentMapper = studentMapper;
        this.studentCourseResultMapper = studentCourseResultMapper;
    }

    public StudentResponse getStudentById(Integer id) {
        return studentMapper.toResponse(studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + id + " not found")
        ));
    }

    public StudentResponse getStudentByName(String name) {
        return studentMapper.toResponse(studentRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Student with a name of " + name + " not found")
        ));
    }

    public StudentResponse getStudentByEmail(String email) {
        return studentMapper.toResponse(studentRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Student with an email of " + email + " not found")
        ));
    }

    public List<StudentCourseResultResponse> getStudentCoursesResultsByStudentId(Integer studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student with an id of " + studentId + " not found");
        }

        return studentCourseResultRepository.findByStudentId(studentId)
                .stream()
                .map(studentCourseResultMapper::toResponse)
                .toList();
    }
}
