package com.example.argusclone.services;

import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.entities.Score;
import com.example.argusclone.entities.StudentCourseResult;
import com.example.argusclone.exceptions.NegativeValueException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.exceptions.ValidationException;
import com.example.argusclone.exceptions.ValueExceedsMaximumException;
import com.example.argusclone.mappers.ScoreMapper;
import com.example.argusclone.repositories.ScoreRepository;
import com.example.argusclone.repositories.StudentCourseResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreService {
    private static final Logger log = LoggerFactory.getLogger(ScoreService.class);

    private final ScoreRepository scoreRepository;
    private final StudentCourseResultRepository studentCourseResultRepository;
    private final ScoreMapper scoreMapper;

    @Autowired
    public ScoreService(ScoreRepository scoreRepository,
                        StudentCourseResultRepository studentCourseResultRepository,
                        ScoreMapper scoreMapper) {
        this.scoreRepository = scoreRepository;
        this.studentCourseResultRepository = studentCourseResultRepository;
        this.scoreMapper = scoreMapper;
    }

    public List<ScoreResponse> getStudentScoresByCourseId(Integer courseId, Integer studentId) {
        List<Score> scores = scoreRepository.findByStudentIdAndCourseId(studentId, courseId);
        return scores.stream().map(scoreMapper::toResponse).toList();
    }

    @Transactional
    public ScoreResponse updateScoreById(Integer courseId, Integer scoreId, Integer score) {
        Score scoreToUpdate = scoreRepository.findById(scoreId).orElseThrow(
                () -> new ResourceNotFoundException("Score with an id of " + scoreId + " not found")
        );

        if (!scoreToUpdate.getCourse().getId().equals(courseId)) {
            throw new ValidationException("Score does not belong to the course with id " + courseId);
        }

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

    @Transactional
    public void deleteScoresByCourseId(Integer courseId) {
        scoreRepository.deleteByCourseId(courseId);
    }
}
