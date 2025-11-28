package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.CourseMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.InstructorRepository;
import com.example.argusclone.services.InstructorCourseAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstructorCourseAssignmentServiceImpl implements InstructorCourseAssignmentService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public CourseResponse assignInstructorToCourse(Integer courseId, Integer instructorId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        course.getInstructors().add(instructorRepository.findById(instructorId).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + instructorId + " not found")
        ));

        return courseMapper.toResponse(courseRepository.save(course));
    }
}
