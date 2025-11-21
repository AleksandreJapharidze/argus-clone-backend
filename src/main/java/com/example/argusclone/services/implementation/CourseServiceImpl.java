package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.course.CreateCourseRequest;
import com.example.argusclone.dtos.syllabus.SyllabusRequest;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Syllabus;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.CourseMapper;
import com.example.argusclone.mappers.SyllabusMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.repositories.InstructorRepository;
import com.example.argusclone.repositories.SyllabusRepository;
import com.example.argusclone.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SyllabusRepository syllabusRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private SyllabusMapper syllabusMapper;

    @Override
    public CourseResponse getCourseById(Integer id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + id + " not found")
        );

        return courseMapper.toResponse(course);
    }

    @Override
    public CourseResponse getCourseByName(String courseName) {
        Course course = courseRepository.findByCourseName(courseName).orElseThrow(
                () -> new ResourceNotFoundException("Course with a name of " + courseName + " not found")
        );

        return courseMapper.toResponse(course);
    }

    @Override
    public CourseResponse getCourseByCourseCode(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode).orElseThrow(
                () -> new ResourceNotFoundException("Course with a code of " + courseCode + " not found")
        );

        return courseMapper.toResponse(course);
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> getCoursesByInstructorId(Integer instructorId) {
        List<Course> instructorCourses = instructorRepository.findById(instructorId).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + instructorId + " not found")
        ).getCourses();

        return instructorCourses.stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public SyllabusResponse getSyllabusByCourseId(Integer courseId) {
        Syllabus syllabus = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        ).getSyllabus();

        return syllabusMapper.toResponse(syllabus);
    }

    @Override
    public CourseResponse addCourse(CreateCourseRequest course) {
        courseRepository.findByCourseCode(course.getCourseCode()).ifPresent(c -> {
            throw new DuplicateResourceException("Course with code " + course.getCourseCode() + " already exists");
        });

        Course newCourse = courseMapper.toEntity(course);
        return courseMapper.toResponse(courseRepository.save(newCourse));
    }

    @Override
    public SyllabusResponse addCourseSyllabus(Integer courseId, SyllabusRequest syllabus) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        Syllabus newSyllabus = syllabusMapper.toEntity(syllabus);
        course.setSyllabus(newSyllabus);
        return syllabusMapper.toResponse(syllabusRepository.save(newSyllabus));
    }

    @Override
    public CourseResponse assignInstructorToCourse(Integer courseId, Integer instructorId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );
        course.getInstructors().add(instructorRepository.findById(instructorId).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + instructorId + " not found")
        ));

        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public void deleteCourseSyllabus(Integer courseId) {
        Syllabus syllabus = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        ).getSyllabus();

        syllabusRepository.delete(syllabus);
    }

    @Override
    public void deleteCourseById(Integer id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course with an id of " + id + " not found");
        }

        courseRepository.deleteById(id);
    }
}
