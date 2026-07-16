package com.example.argusclone.services;

import com.example.argusclone.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseStudentService {
    private final CourseRepository courseRepository;

    public CourseStudentService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    protected List<Integer> getCourseIdsByStudentId(Integer studentId) {
        return courseRepository.findAllCourseIdsByStudentId(studentId);
    }
}
