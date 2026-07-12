package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.entities.Course;
import com.example.argusclone.exceptions.ValidationException;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.ScoreRepository;
import com.example.argusclone.services.ScoreGenerationService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreGenerationServiceImpl implements ScoreGenerationService {
    private final ScoreRepository scoreRepository;
    private final CourseRepository courseRepository;
    private final JdbcTemplate jdbcTemplate;

    public ScoreGenerationServiceImpl(ScoreRepository scoreRepository,
                                      CourseRepository courseRepository,
                                      JdbcTemplate jdbcTemplate) {
        this.scoreRepository = scoreRepository;
        this.courseRepository = courseRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String generateDefaultScoresForStudentsInCourse(Integer courseId, List<CreateScoreRequest> scores) {
        validateScores(scores);

        if (scoreRepository.existsByCourseId(courseId)) {
            throw new ValidationException("Scores for this course already exist");
        }

        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ValidationException("Course with an id of " + courseId + " not found")
        );

        List<ScoreRow> scoreRows = createScoreRows(course, scores);

        try {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO score (component, max_score, threshold, score, course_id, student_id, course_name, student_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    scoreRows,
                    500,
                    (ps, row) -> {
                        ps.setString(1, row.component());
                        ps.setInt(2, row.maxScore());
                        ps.setInt(3, row.threshold());
                        ps.setInt(4, row.score());
                        ps.setInt(5, row.courseId());
                        ps.setInt(6, row.studentId());
                        ps.setString(7, row.courseName());
                        ps.setString(8, row.studentName());
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "Scores generated successfully for course with id of " + courseId;
    }

    private List<ScoreRow> createScoreRows(Course course, List<CreateScoreRequest> scores) {
        List<StudentIdAndName> studentsIdsAndNames = getStudentsByCourseId(course.getId());

        List<ScoreRow> scoreRows = new ArrayList<>();
        for (CreateScoreRequest scoreRequest : scores) {
            for (StudentIdAndName studentIdAndName : studentsIdsAndNames) {
                ScoreRow scoreRow = new ScoreRow(
                        scoreRequest.component(),
                        scoreRequest.maxScore(),
                        scoreRequest.threshold(),
                        0,
                        course.getId(),
                        studentIdAndName.studentId(),
                        course.getCourseName(),
                        studentIdAndName.studentName()
                );
                scoreRows.add(scoreRow);
            }
        }

        return scoreRows;
    }

    private List<StudentIdAndName> getStudentsByCourseId(Integer courseId) {
        return jdbcTemplate.query(
                "SELECT DISTINCT s.id, s.name FROM student s " +
                        "JOIN group_student gs " +
                        "ON s.id = gs.student_id " +
                        "JOIN course_group cg " +
                        "ON gs.group_id = cg.id " +
                        "WHERE cg.course_id = ?",
                (resultSet, rowNum) -> new StudentIdAndName(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ),
                courseId
        );
    }

    private void validateScores(List<CreateScoreRequest> scores) {
        boolean containsFinalExam = false;
        int totalScore = 0;
        for (CreateScoreRequest score : scores) {
            if (score.component().equalsIgnoreCase("Final exam")) {
                containsFinalExam = true;
            }
            totalScore += score.maxScore();
        }

        if (totalScore != 100) {
            throw new ValidationException("All max scores should sum up to 100");
        }

        if (!containsFinalExam) {
            throw new ValidationException("Every list of scores should contain final exam");
        }
    }

    private record StudentIdAndName(Integer studentId, String studentName) {}

    private record ScoreRow(String component,
                            Integer maxScore,
                            Integer threshold,
                            Integer score,
                            Integer courseId,
                            Integer studentId,
                            String courseName,
                            String studentName) {}
}
