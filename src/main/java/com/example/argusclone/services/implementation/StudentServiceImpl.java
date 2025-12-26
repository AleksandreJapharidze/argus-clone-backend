package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.result.StudentCourseResultResponse;
import com.example.argusclone.dtos.resumeservice.StudentRequestForResumeService;
import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.entities.Student;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.HttpClientErrorException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.StudentCourseResultMapper;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.ScoreRepository;
import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    private final StudentCourseResultRepository studentCourseResultRepository;
    private final StudentMapper studentMapper;
    private final StudentCourseResultMapper studentCourseResultMapper;

    @Value("${resume.service.url}")
    private String resumeServiceUrl;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              ScoreRepository scoreRepository,
                              StudentCourseResultRepository studentCourseResultRepository,
                              StudentMapper studentMapper,
                              StudentCourseResultMapper studentCourseResultMapper) {
        this.studentRepository = studentRepository;
        this.scoreRepository = scoreRepository;
        this.studentCourseResultRepository = studentCourseResultRepository;
        this.studentMapper = studentMapper;
        this.studentCourseResultMapper = studentCourseResultMapper;
    }

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
    public List<StudentCourseResultResponse> getStudentCoursesResultsByStudentId(Integer studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student with an id of " + studentId + " not found");
        }

        return studentCourseResultRepository.findByStudentId(studentId)
                .stream()
                .map(studentCourseResultMapper::toResponse)
                .toList();
    }

    @Override
    public StudentResponse addStudent(CreateStudentRequest student) {
        studentRepository.findByEmail(student.getEmail()).ifPresent(s -> {
            throw new DuplicateResourceException("Student with email " + student.getEmail() + " already exists");
        });

        addStudentForResumeService(student);

        Student newStudent = studentMapper.toEntity(student);
        return studentMapper.toResponse(studentRepository.save(newStudent));
    }

    private void addStudentForResumeService(CreateStudentRequest student) {
        StudentRequestForResumeService requestForResumeService = new StudentRequestForResumeService(
                student.getName(), student.getEmail()
        );

        final RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForObject(resumeServiceUrl + "/students", requestForResumeService, StudentRequestForResumeService.class);
        } catch (Exception e) {
            throw new HttpClientErrorException("Failed to add student to resume service");
        }
    }

    @Override
    public void deleteStudentById(Integer id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + id + " not found")
        );

        student.getGroups().forEach(group -> group.getStudents().remove(student));
//        student.getScores().forEach(score -> score.setStudent(null));
//        student.getStudentCourseResults().forEach(studentCourseResult -> studentCourseResult.setStudent(null));
//        student.getStudentCourseResults().forEach(studentCourseResult -> studentCourseResult.setCourse(null));

        scoreRepository.deleteAll(student.getScores());
        studentCourseResultRepository.deleteAll(student.getStudentCourseResults());
        studentRepository.deleteById(id);
    }
}
