package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Score;
import com.example.argusclone.entities.StudentCourseResult;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.ScoreMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.ScoreRepository;
import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final CourseRepository courseRepository;
    private final StudentCourseResultRepository studentCourseResultRepository;
    private final ScoreMapper scoreMapper;

    @Autowired
    public ScoreServiceImpl(ScoreRepository scoreRepository, CourseRepository courseRepository,
                            StudentCourseResultRepository studentCourseResultRepository, ScoreMapper scoreMapper) {
        this.scoreRepository = scoreRepository;
        this.courseRepository = courseRepository;
        this.studentCourseResultRepository = studentCourseResultRepository;
        this.scoreMapper = scoreMapper;
    }

    @Override
    public List<ScoreResponse> getStudentScoresByCourseId(Integer courseId, Integer studentId) {
        List<Score> scores = scoreRepository.findByStudentIdAndCourseId(studentId, courseId);
        return scores.stream().map(scoreMapper::toResponse).toList();
    }

    @Override
    public List<ScoreResponse> generateEmptyListsOfScoresForStudentsByCourseId(Integer courseId, List<CreateScoreRequest> scores) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        List<Score> scoresSaved = course.getGroups().stream()
                .flatMap(group -> group.getStudents().stream())
                .flatMap(student -> scores.stream().map(scoreRequest -> {
                    Score score = scoreMapper.toEntity(scoreRequest);
                    score.setStudent(student);
                    score.setCourse(course);
                    score.setCourseName(course.getCourseName());
                    score.setStudentName(student.getName());
                    return score;
                })).toList();

        course.setScores(scoresSaved);
        course.getGroups()
                .forEach(group -> group.getStudents()
                .forEach(student -> student.setScores(scoresSaved)));

        return scoreRepository.saveAll(scoresSaved)
                .stream()
                .map(scoreMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ScoreResponse updateScoreById(Integer scoreId, Integer score) {
        Score scoreToUpdate = scoreRepository.findById(scoreId).orElseThrow(
                () -> new ResourceNotFoundException("Score with an id of " + scoreId + " not found")
        );

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
    }

    private StudentCourseResult calculateStudentCourseResult(Score scoreToUpdate, Integer score) {
        StudentCourseResult result = new StudentCourseResult();
        result.setStudent(scoreToUpdate.getStudent());
        result.setCourse(scoreToUpdate.getCourse());
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
        Integer courseId = scoreToUpdate.getCourse().getId();

        Optional<StudentCourseResult> existingResult = studentCourseResultRepository.findByStudentIdAndCourseId(studentId, courseId);

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

    @Override
    public void deleteScoresByCourseId(Integer courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        course.getScores().forEach(score -> score.setCourse(null));
        scoreRepository.deleteAll(course.getScores());
    }
}
