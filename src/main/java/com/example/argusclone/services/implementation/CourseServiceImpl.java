package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.course.CreateCourseRequest;
import com.example.argusclone.entities.Course;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.CourseMapper;
import com.example.argusclone.repositories.*;
import com.example.argusclone.services.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    @Cacheable(value = "COURSE_CACHE", key = "'id: ' + #id")
    public CourseResponse getCourseById(Integer id) {
        return courseMapper.toResponse(courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + id + " not found")
        ));
    }

    @Override
    @Cacheable(value = "COURSE_CACHE_LIST", key = "'allCourses: ' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public List<CourseResponse> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "COURSE_CACHE_LIST", key = "'searchKeyword: ' + #keyword")
    public List<CourseResponse> searchCourses(String keyword) {
        log.info("Searching for courses with keyword: {}", keyword);
        return courseRepository.searchCourses(keyword)
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "COURSE_CACHE_LIST", key = "'instructorId: ' + #instructorId")
    public List<CourseResponse> getCoursesByInstructorId(Integer instructorId) {
        List<Course> instructorCourses = courseRepository.findAllByInstructorId(instructorId);

        return instructorCourses.stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "COURSE_CACHE_LIST", key = "'studentId: ' + #studentId")
    public List<CourseResponse> getCoursesByStudentId(Integer studentId) {
        List<Course> courses = courseRepository.findCoursesByStudentId(studentId);

        return courses.stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    @CachePut(value = "COURSE_CACHE", key = "'id: ' + #result.id")
    public CourseResponse addCourse(CreateCourseRequest course) {
        courseRepository.findByCourseCode(course.getCourseCode()).ifPresent(c -> {
            throw new DuplicateResourceException("Course with code " + course.getCourseCode() + " already exists");
        });

        Course newCourse = courseMapper.toEntity(course);
        return courseMapper.toResponse(courseRepository.save(newCourse));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "COURSE_CACHE", key = "'id: ' + #id"),
            @CacheEvict(value = "COURSE_CACHE_LIST", allEntries = true),
            @CacheEvict(value = "SYLLABUS_CACHE", key = "'courseId: ' + #id")
    })
    public void deleteCourseById(Integer id) {
        try {
            courseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Course with an id of " + id + " not found");
        }
    }
}
