package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.course.CreateCourseRequest;
import com.example.argusclone.entities.Course;
import com.example.argusclone.mappers.CourseMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.repositories.InstructorRepository;
import com.example.argusclone.repositories.SyllabusRepository;
import com.example.argusclone.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SyllabusRepository syllabusRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public CourseResponse getCourseById(Integer id) {
        Course course = courseRepository.findById(id).orElseThrow();
        return courseMapper.toResponse(course);
    }

    @Override
    public CourseResponse getCourseByName(String name) {
        Course course = courseRepository.findByName(name).orElseThrow();
        return courseMapper.toResponse(course);
    }

    @Override
    public CourseResponse getCourseByCourseCode(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode).orElseThrow();
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
    public CourseResponse addCourse(CreateCourseRequest course) {
        courseRepository.findByCourseCode(course.getCourseCode()).ifPresent(c -> {
            throw new IllegalArgumentException("Course with code " + course.getCourseCode() + " already exists");
        });

        Course newCourse = courseMapper.toEntity(course);;
        return courseMapper.toResponse(courseRepository.save(newCourse));
    }

    @Override
    public CourseResponse assignInstructorToCourse(Integer courseId, Integer instructorId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        course.getInstructors().add(instructorRepository.findById(instructorId).orElseThrow());
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public void deleteCourseById(Integer id) {
        if (!courseRepository.existsById(id)) {
            return;
        }

        courseRepository.deleteById(id);
    }
}
