package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.course.CreateCourseRequest;
import com.example.argusclone.dtos.syllabus.SyllabusRequest;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Group;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.CourseMapper;
import com.example.argusclone.repositories.*;
import com.example.argusclone.services.CourseService;
import com.example.argusclone.services.SyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private SyllabusService syllabusService;

    @Override
    public CourseResponse getCourseById(Integer id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + id + " not found")
        );

        return courseMapper.toResponse(course);
    }

    @Override
    public CourseResponse getCourseByName(String courseName) {
        Course course = courseRepository.findByCourseName(courseName).orElseThrow(
                () -> new ResourceNotFoundException("Course with a name of " + courseName + " not found")
        );

        return courseMapper.toResponse(course);
    }

    @Override
    public CourseResponse getCourseByCourseCode(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode).orElseThrow(
                () -> new ResourceNotFoundException("Course with a code of " + courseCode + " not found")
        );

        return courseMapper.toResponse(course);
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> getCoursesByInstructorId(Integer instructorId) {
        List<Course> instructorCourses = instructorRepository.findById(instructorId).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + instructorId + " not found")
        ).getCourses();

        return instructorCourses.stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> getCoursesByStudentId(Integer studentId) {
        List<Course> studentCourses = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + studentId + " not found")
        ).getGroups().stream()
                .map(Group::getCourse)
                .toList();

        Set<Course> studentCoursesSet = new HashSet<>(studentCourses);
        return studentCoursesSet.stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public SyllabusResponse getCourseSyllabus(Integer courseId) {
        return syllabusService.getSyllabusByCourseId(courseId);
    }

    @Override
    public CourseResponse addCourse(CreateCourseRequest course) {
        courseRepository.findByCourseCode(course.getCourseCode()).ifPresent(c -> {
            throw new DuplicateResourceException("Course with code " + course.getCourseCode() + " already exists");
        });

        Course newCourse = courseMapper.toEntity(course);
        return courseMapper.toResponse(courseRepository.save(newCourse));
    }

    @Override
    public SyllabusResponse addCourseSyllabus(Integer courseId, SyllabusRequest syllabus) {
        return syllabusService.addSyllabusToCourse(courseId, syllabus);
    }

    @Override
    public void deleteCourseById(Integer id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course with an id of " + id + " not found");
        }

        courseRepository.deleteById(id);
    }

    @Override
    public void deleteCourseSyllabus(Integer courseId) {
        syllabusService.deleteSyllabusByCourseId(courseId);
    }
}
