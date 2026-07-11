package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Score;
import com.example.argusclone.entities.StudentCourseResult;
import com.example.argusclone.exceptions.NegativeValueException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.exceptions.ValidationException;
import com.example.argusclone.exceptions.ValueExceedsMaximumException;
import com.example.argusclone.mappers.ScoreMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.ScoreRepository;
import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.services.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScoreServiceImpl implements ScoreService {
    Logger log = LoggerFactory.getLogger(ScoreServiceImpl.class);

    private final ScoreRepository scoreRepository;
    private final CourseRepository courseRepository;
    private final StudentCourseResultRepository studentCourseResultRepository;
    private final ScoreMapper scoreMapper;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ScoreServiceImpl(ScoreRepository scoreRepository,
                            CourseRepository courseRepository,
                            StudentCourseResultRepository studentCourseResultRepository,
                            ScoreMapper scoreMapper,
                            JdbcTemplate jdbcTemplate) {
        this.scoreRepository = scoreRepository;
        this.courseRepository = courseRepository;
        this.studentCourseResultRepository = studentCourseResultRepository;
        this.scoreMapper = scoreMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ScoreResponse> getStudentScoresByCourseId(Integer courseId, Integer studentId) {
        List<Score> scores = scoreRepository.findByStudentIdAndCourseId(studentId, courseId);
        return scores.stream().map(scoreMapper::toResponse).toList();
    }

    @Override
    public String generateEmptyListsOfScoresForStudentsByCourseId(Integer courseId, List<CreateScoreRequest> scores) {
        validateScores(scores);

        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        if (scoreRepository.existsByCourseId(courseId)) {
            throw new ValidationException("Scores for this course already exist");
        }

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

    @Override
    @Transactional
    public ScoreResponse updateScoreById(Integer scoreId, Integer score) {
        Score scoreToUpdate = scoreRepository.findById(scoreId).orElseThrow(
                () -> new ResourceNotFoundException("Score with an id of " + scoreId + " not found")
        );

        if (!isNotMoreThanMaxScore(scoreToUpdate, score)) {
            throw new ValueExceedsMaximumException("Score can not be more than maximum score");
        } else if (score < 0) {
            throw new NegativeValueException("Score can not be negative");
        }

        updateScoreValue(scoreToUpdate, score);

        if (scoreToUpdate.getComponent().equalsIgnoreCase("Final exam")) {
            StudentCourseResult result = calculateStudentCourseResult(scoreToUpdate, score);
            saveOrUpdateCourseResult(scoreToUpdate, result);
        }

        return scoreMapper.toResponse(scoreRepository.save(scoreToUpdate));
    }

    private void updateScoreValue(Score scoreToUpdate, Integer score) {
        Integer threshold = scoreToUpdate.getThreshold();
        if (threshold != null && score < threshold) {
            scoreToUpdate.setScore(0);
        } else {
            scoreToUpdate.setScore(score);
        }

        updateCourseResultAfterNonFinalExamScoreUpdateIfNecessary(scoreToUpdate);
    }

    private void updateCourseResultAfterNonFinalExamScoreUpdateIfNecessary(Score scoreToUpdate) {
        studentCourseResultRepository.findByStudentIdAndCourseName(scoreToUpdate.getStudent().getId(), scoreToUpdate.getCourse().getCourseName())
                .ifPresentOrElse(
                        result -> {
                            if (result.getFinalGrade() == null || scoreToUpdate.getComponent().equalsIgnoreCase("Final exam")) {
                                return;
                            }

                            int newFinalGrade = calculateTotalCourseScoreForStudent(scoreToUpdate);
                            if (newFinalGrade < 51) {
                                result.setFinalGrade(newFinalGrade);
                                result.setHasPassed(false);
                            } else {
                                result.setFinalGrade(newFinalGrade);
                                result.setHasPassed(true);
                            }
                            studentCourseResultRepository.save(result);

                            log.info("Course result for student with id of {} and course with id of {} has been updated",
                                    scoreToUpdate.getStudent().getId(), scoreToUpdate.getCourse().getId());
                        },
                        () -> log.info("Course result for student with id of {} and course with id of {} doesn't exist for updating",
                                scoreToUpdate.getStudent().getId(), scoreToUpdate.getCourse().getId())
                );
    }

    private StudentCourseResult calculateStudentCourseResult(Score scoreToUpdate, Integer score) {
        StudentCourseResult result = new StudentCourseResult();
        result.setStudent(scoreToUpdate.getStudent());
        result.setCourseName(scoreToUpdate.getCourseName());
        result.setStudentName(scoreToUpdate.getStudentName());

        if (!meetsThreshold(scoreToUpdate, score)) {
            result.setHasPassed(false);
            return result;
        }

        int totalCourseScoreForStudent = calculateTotalCourseScoreForStudent(scoreToUpdate);
        if (totalCourseScoreForStudent >= 51) {
            result.setHasPassed(true);
            result.setFinalGrade(totalCourseScoreForStudent);
        } else {
            result.setHasPassed(false);
            result.setFinalGrade(totalCourseScoreForStudent);
        }

        return result;
    }

    private int calculateTotalCourseScoreForStudent(Score score) {
        return score.getStudent().getScores().stream()
                .filter(s -> s.getCourse().equals(score.getCourse()))
                .mapToInt(Score::getScore)
                .sum();
    }

    private void saveOrUpdateCourseResult(Score scoreToUpdate, StudentCourseResult updateResult) {
        Integer studentId = scoreToUpdate.getStudent().getId();
        String courseName = scoreToUpdate.getCourse().getCourseName();

        Optional<StudentCourseResult> existingResult = studentCourseResultRepository.findByStudentIdAndCourseName(studentId, courseName);

        if (existingResult.isPresent()) {
            StudentCourseResult existingResultToUpdate = existingResult.get();

            existingResultToUpdate.setHasPassed(updateResult.getHasPassed());
            existingResultToUpdate.setFinalGrade(updateResult.getFinalGrade());
            existingResultToUpdate.setStudentName(updateResult.getStudentName());
            existingResultToUpdate.setCourseName(updateResult.getCourseName());

            studentCourseResultRepository.save(existingResultToUpdate);
        } else {
            scoreToUpdate.getStudent().getStudentCourseResults().add(updateResult);
            studentCourseResultRepository.save(updateResult);
        }
    }

    private boolean meetsThreshold(Score scoreToUpdate, int score) {
        return scoreToUpdate.getThreshold() == null || score >= scoreToUpdate.getThreshold();
    }

    private boolean isNotMoreThanMaxScore(Score scoreToUpdate, int score) {
        return score <= scoreToUpdate.getMaxScore();
    }

    @Override
    @Transactional
    public void deleteScoresByCourseId(Integer courseId) {
        scoreRepository.deleteByCourseId(courseId);
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
