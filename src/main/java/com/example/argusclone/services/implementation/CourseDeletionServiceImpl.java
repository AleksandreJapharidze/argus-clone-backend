package com.example.argusclone.services.implementation;

import com.example.argusclone.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseDeletionServiceImpl implements CourseDeletionService {
    private final CourseService courseService;
    private final LectureService lectureService;
    private final ScoreService scoreService;

    @Autowired
    public CourseDeletionServiceImpl(CourseService courseService,
                                     LectureService lectureService,
                                     ScoreService scoreService) {
        this.courseService = courseService;
        this.lectureService = lectureService;
        this.scoreService = scoreService;
    }

    @Override
    @Transactional
    public void deleteCourseById(Integer id) {
        scoreService.deleteScoresByCourseId(id);
        lectureService.deleteLecturesByCourseId(id);
        courseService.deleteCourseById(id);
    }
}
