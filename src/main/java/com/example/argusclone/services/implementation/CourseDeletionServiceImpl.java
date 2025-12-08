package com.example.argusclone.services.implementation;

import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.services.CourseDeletionService;
import com.example.argusclone.services.CourseService;
import com.example.argusclone.services.GroupService;
import com.example.argusclone.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseDeletionServiceImpl implements CourseDeletionService {
    private final CourseService courseService;
    private final GroupService groupService;
    private final ScoreService scoreService;
    private final StudentCourseResultRepository studentCourseResultRepository;

    @Autowired
    public CourseDeletionServiceImpl(CourseService courseService, GroupService groupService, ScoreService scoreService,
                                     StudentCourseResultRepository studentCourseResultRepository) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.scoreService = scoreService;
        this.studentCourseResultRepository = studentCourseResultRepository;
    }

    @Override
    public void deleteCourseById(Integer id) {
        groupService.deleteLecturesByCourseId(id);
        scoreService.deleteScoresByCourseId(id);
        studentCourseResultRepository.deleteByCourseId(id);
        groupService.deleteGroupsByCourseId(id);
        courseService.deleteCourseSyllabus(id);
        courseService.deleteCourseById(id);
    }
}
