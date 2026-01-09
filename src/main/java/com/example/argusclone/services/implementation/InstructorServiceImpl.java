package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.instructor.CreateInstructorRequest;
import com.example.argusclone.dtos.instructor.InstructorResponse;
import com.example.argusclone.entities.Instructor;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.InstructorMapper;
import com.example.argusclone.repositories.InstructorRepository;
import com.example.argusclone.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository,
                                 InstructorMapper instructorMapper) {
        this.instructorRepository = instructorRepository;
        this.instructorMapper = instructorMapper;
    }

    @Override
    @Cacheable(value = "INSTRUCTOR_CACHE", key = "'id: ' + #id")
    public InstructorResponse getInstructorById(Integer id) {
        return instructorMapper.toResponse(instructorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + id + " not found")
        ));
    }

    @Override
    @CachePut(value = "INSTRUCTOR_CACHE", key = "'id: ' + #result.getId()")
    public InstructorResponse addInstructor(CreateInstructorRequest instructor) {
        instructorRepository.findByEmail(instructor.getEmail()).ifPresent(i -> {
            throw new DuplicateResourceException("Instructor with email " + instructor.getEmail() + " already exists");
        });

        Instructor newInstructor = instructorMapper.toEntity(instructor);
        return instructorMapper.toResponse(instructorRepository.save(newInstructor));
    }

    @Override
    @CacheEvict(value = "INSTRUCTOR_CACHE", key = "'id: ' + #id")
    public void deleteInstructorById(Integer id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + id + " not found")
        );

        instructor.getCourses().forEach(course -> course.getInstructors().remove(instructor));
        instructorRepository.deleteById(id);
    }
}
