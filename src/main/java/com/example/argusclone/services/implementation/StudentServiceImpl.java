package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Score;
import com.example.argusclone.entities.Student;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.ScoreMapper;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.ScoreRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    ScoreMapper scoreMapper;

    @Override
    public StudentResponse getStudentById(Integer id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + id + " not found")
        );

        return studentMapper.toResponse(student);
    }

    @Override
    public StudentResponse getStudentByName(String name) {
        Student student = studentRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Student with a name of " + name + " not found")
        );

        return studentMapper.toResponse(student);
    }

    @Override
    public StudentResponse getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Student with an email of " + email + " not found")
        );

        return studentMapper.toResponse(student);
    }

    @Override
    public StudentResponse addStudent(CreateStudentRequest student) {
        studentRepository.findByEmail(student.getEmail()).ifPresent(s -> {
            throw new DuplicateResourceException("Student with email " + student.getEmail() + " already exists");
        });

        Student newStudent = studentMapper.toEntity(student);
        return studentMapper.toResponse(studentRepository.save(newStudent));
    }

    @Override
    public ScoreResponse addScoreToStudent(Integer studentId, Integer courseId, CreateScoreRequest score) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + studentId + " not found")
        );

        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        Score newScore = scoreMapper.toEntity(score);
        newScore.setCourse(course);
        newScore.setStudent(student);

        student.getScores().add(newScore);
        course.getScores().add(newScore);
        return scoreMapper.toResponse(scoreRepository.save(newScore));
    }

    @Override
    public void deleteStudentById(Integer id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + id + " not found")
        );

        student.getGroups().forEach(group -> group.getStudents().remove(student));
        studentRepository.deleteById(id);
    }
}
