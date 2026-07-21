package com.example.argusclone.services;

import com.example.argusclone.dtos.instructor.CreateInstructorRequest;
import com.example.argusclone.dtos.instructor.InstructorResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Instructor;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.InstructorMapper;
import com.example.argusclone.repositories.InstructorRepository;
import com.example.argusclone.services.caching.InstructorRelatedCacheService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;
    private final InstructorRelatedCacheService instructorRelatedCacheService;

    public InstructorService(InstructorRepository instructorRepository,
                             InstructorMapper instructorMapper,
                             InstructorRelatedCacheService instructorRelatedCacheService) {
        this.instructorRepository = instructorRepository;
        this.instructorMapper = instructorMapper;
        this.instructorRelatedCacheService = instructorRelatedCacheService;
    }

    @Cacheable(cacheNames = "instructor-cache", key = "#id")
    public InstructorResponse getInstructorById(Integer id) {
        return instructorMapper.toResponse(instructorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + id + " not found")
        ));
    }

    @CachePut(cacheNames = "instructor-cache", key = "#result.id()")
    @Transactional
    public InstructorResponse addInstructor(CreateInstructorRequest instructor) {
        final String email = instructor.email();
        instructorRepository.findByEmail(email).ifPresent(i -> {
            throw new DuplicateResourceException("Instructor with email " + email + " already exists");
        });

        Instructor newInstructor = instructorMapper.toEntity(instructor);
        Instructor savedInstructor = instructorRepository.save(newInstructor);

        return instructorMapper.toResponse(savedInstructor);
    }

    @CacheEvict(cacheNames = "instructor-cache", key = "#id")
    @Transactional
    public void deleteInstructorById(Integer id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + id + " not found")
        );

        Set<Course> courses = instructor.getCourses();

        List<Integer> courseIds = courses.stream().map(Course::getId).toList();
        instructorRelatedCacheService.clearAllRelevantCachesForInstructor(id, courseIds);

        courses.forEach(course -> course.getInstructors().remove(instructor));
        instructorRepository.delete(instructor);
    }
}
