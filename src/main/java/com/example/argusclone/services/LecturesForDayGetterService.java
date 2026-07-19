package com.example.argusclone.services;

import com.example.argusclone.dtos.lecture.LectureForDay;
import com.example.argusclone.exceptions.NotAMemberException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LecturesForDayGetterService {
    private final GroupStudentsService groupStudentsService;
    private final CourseInstructorService courseInstructorService;
    private final JdbcTemplate jdbcTemplate;

    public LecturesForDayGetterService(GroupStudentsService groupStudentsService,
                                       CourseInstructorService courseInstructorService,
                                       JdbcTemplate jdbcTemplate) {
        this.groupStudentsService = groupStudentsService;
        this.courseInstructorService = courseInstructorService;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<LectureForDay> getLecturesForDayForStudent(Integer groupId, Integer studentId, LocalDate lectureDate) {
        List<Integer> studentGroupIds = groupStudentsService.getAllGroupIdsByStudentId(studentId);
        if (!studentGroupIds.contains(groupId)) {
            throw new NotAMemberException("Student is not a member of the group with id " + groupId);
        }

        return jdbcTemplate.query(
                "SELECT l.lecture_date, l.lecture_start_time, l.lecture_end_time, l.room_number, c.course_name, cg.group_name " +
                        "FROM lecture l " +
                        "JOIN course_group cg ON l.group_id = cg.id " +
                        "JOIN course c ON cg.course_id = c.id " +
                        "WHERE l.lecture_date = ? AND l.group_id = ?",
                (resultSet, rowNum) -> new LectureForDay(
                        resultSet.getDate("lecture_date").toLocalDate(),
                        resultSet.getTime("lecture_start_time").toLocalTime(),
                        resultSet.getTime("lecture_end_time").toLocalTime(),
                        resultSet.getString("room_number"),
                        resultSet.getString("course_name"),
                        resultSet.getString("group_name")
                ),
                lectureDate, groupId
        );
    }

    public List<LectureForDay> getLecturesForDayForInstructor(Integer courseId, Integer instructorId, LocalDate lectureDate) {
        List<Integer> courseIds = courseInstructorService.getCourseIdsByInstructorId(instructorId);
        if (!courseIds.contains(courseId)) {
            throw new NotAMemberException("The instructor is not instructor of the course with id " + courseId);
        }

        return jdbcTemplate.query(
                "SELECT l.lecture_date, l.lecture_start_time, l.lecture_end_time, l.room_number, c.course_name, cg.group_name " +
                        "FROM lecture l " +
                        "JOIN course_group cg ON l.group_id = cg.id " +
                        "JOIN course c ON cg.course_id = c.id " +
                        "WHERE cg.course_id = ? AND l.lecture_date = ?",
                (resultSet, rowNum) -> new LectureForDay(
                        resultSet.getDate("lecture_date").toLocalDate(),
                        resultSet.getTime("lecture_start_time").toLocalTime(),
                        resultSet.getTime("lecture_end_time").toLocalTime(),
                        resultSet.getString("room_number"),
                        resultSet.getString("course_name"),
                        resultSet.getString("group_name")
                ),
                courseId, lectureDate
        );
    }
}
