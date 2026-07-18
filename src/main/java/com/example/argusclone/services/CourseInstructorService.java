package com.example.argusclone.services;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Instructor;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.CourseMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseInstructorService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseInstructorService(CourseRepository courseRepository,
                                   InstructorRepository instructorRepository,
                                   CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.courseMapper = courseMapper;
    }

    public CourseResponse assignInstructorToCourse(Integer courseId, Integer instructorId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + instructorId + " not found")
        );

        if (!course.getInstructors().contains(instructor)) {
            course.getInstructors().add(instructor);
        } else {
            throw new DuplicateResourceException("Instructor already assigned to course");
        }

        return courseMapper.toResponse(courseRepository.save(course));
    }

    public void removeInstructorFromCourse(Integer courseId, Integer instructorId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + instructorId + " not found")
        );

        if (!course.getInstructors().contains(instructor)) {
            throw new ResourceNotFoundException("Instructor not assigned to course");
        }

        course.getInstructors().remove(instructor);
        courseRepository.save(course);
    }

    public List<Integer> getCourseIdsByInstructorId(Integer instructorId) {
        return courseRepository.findAllCourseIdsByInstructorId(instructorId);
    }

    public List<Integer> getInstructorIdsByCourseId(Integer courseId) {
        return instructorRepository.findInstructorIdsByCourseId(courseId);
    }
}
