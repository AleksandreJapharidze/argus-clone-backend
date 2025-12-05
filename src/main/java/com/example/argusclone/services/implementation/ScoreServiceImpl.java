package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Score;
import com.example.argusclone.entities.Student;
import com.example.argusclone.entities.StudentCourseResult;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.ScoreMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.ScoreRepository;
import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final StudentCourseResultRepository studentCourseResultRepository;
    private final ScoreMapper scoreMapper;

    @Autowired
    public ScoreServiceImpl(ScoreRepository scoreRepository, CourseRepository courseRepository,
                            StudentCourseResultRepository studentCourseResultRepository,
                            StudentRepository studentRepository, ScoreMapper scoreMapper) {
        this.scoreRepository = scoreRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.studentCourseResultRepository = studentCourseResultRepository;
        this.scoreMapper = scoreMapper;
    }

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

        if (scoreToUpdate.getThreshold() != null) {
            if (score >= scoreToUpdate.getThreshold()) {
                scoreToUpdate.setScore(score);
            } else {
                scoreToUpdate.setScore(0);
            }
        }

        scoreToUpdate.setScore(score);
        if (scoreToUpdate.getComponent().equalsIgnoreCase("Final exam")) {
            StudentCourseResult studentCourseResult = new StudentCourseResult();
            studentCourseResult.setStudent(scoreToUpdate.getStudent());
            studentCourseResult.setCourse(scoreToUpdate.getCourse());
            studentCourseResult.setCourseName(scoreToUpdate.getCourseName());
            studentCourseResult.setStudentName(scoreToUpdate.getStudentName());

            if (score >= scoreToUpdate.getThreshold()) {
                int sumOfStudentScoresInCourse = scoreToUpdate.getStudent().getScores()
                        .stream().filter(s -> s.getCourse().equals(scoreToUpdate.getCourse()))
                        .mapToInt(Score::getScore).sum();
                if (sumOfStudentScoresInCourse >= 51) {
                    studentCourseResult.setHasPassed(true);
                    studentCourseResult.setFinalGrade(sumOfStudentScoresInCourse);
                } else {
                    studentCourseResult.setHasPassed(false);
                }
            } else {
                studentCourseResult.setHasPassed(false);
            }
            scoreToUpdate.getStudent().getStudentCourseResults().add(studentCourseResult);
            studentCourseResultRepository.save(studentCourseResult);
        }

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
