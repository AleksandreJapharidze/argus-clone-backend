package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Score;
import com.example.argusclone.entities.Student;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.ScoreMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.ScoreRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScoreMapper scoreMapper;

    @Override
    public List<ScoreResponse> getStudentScoresByCourseId(Integer courseId, Integer studentId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student with an id of " + studentId + " not found")
        );

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
    public ScoreResponse updateScoreById(Integer scoreId, Integer score) {
        Score scoreToUpdate = scoreRepository.findById(scoreId).orElseThrow(
                () -> new ResourceNotFoundException("Score with an id of " + scoreId + " not found")
        );

        scoreToUpdate.setScore(score);
        return scoreMapper.toResponse(scoreRepository.save(scoreToUpdate));
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
