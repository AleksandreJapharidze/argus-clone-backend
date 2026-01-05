package com.example.argusclone.services.implementation;

import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.services.CourseDeletionService;
import com.example.argusclone.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseDeletionServiceImpl implements CourseDeletionService {
    private final CourseService courseService;
    private final StudentCourseResultRepository studentCourseResultRepository;

    @Autowired
    public CourseDeletionServiceImpl(CourseService courseService, StudentCourseResultRepository studentCourseResultRepository) {
        this.courseService = courseService;
        this.studentCourseResultRepository = studentCourseResultRepository;
    }

    @Override
    @Transactional
    public void deleteCourseById(Integer id) {
        studentCourseResultRepository.deleteByCourseId(id);
        courseService.deleteCourseById(id);
    }
}
