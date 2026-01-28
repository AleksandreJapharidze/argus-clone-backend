package com.example.argusclone.services.implementation;

import com.example.argusclone.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseDeletionServiceImpl implements CourseDeletionService {
    private final CourseService courseService;
    private final GroupService groupService;
    private final LectureService lectureService;
    private final ScoreService scoreService;

    @Autowired
    public CourseDeletionServiceImpl(CourseService courseService,
                                     GroupService groupService,
                                     LectureService lectureService,
                                     ScoreService scoreService) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.lectureService = lectureService;
        this.scoreService = scoreService;
    }

    @Override
    @Transactional
    @CacheEvict(value = "SYLLABUS_CACHE", key = "'courseId: ' + #id")
    public void deleteCourseById(Integer id) {
        courseService.deleteStudentCourseResultByCourseId(id);
        scoreService.deleteScoresByCourseId(id);
        lectureService.deleteLecturesByCourseId(id);
        groupService.deleteGroupCachesByCourseId(id);
        courseService.deleteCourseById(id);
    }
}
