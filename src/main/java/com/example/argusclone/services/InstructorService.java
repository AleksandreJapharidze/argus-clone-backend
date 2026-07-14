package com.example.argusclone.services;

import com.example.argusclone.dtos.instructor.CreateInstructorRequest;
import com.example.argusclone.dtos.instructor.InstructorResponse;
import com.example.argusclone.entities.Instructor;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.InstructorMapper;
import com.example.argusclone.repositories.InstructorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;

    public InstructorService(InstructorRepository instructorRepository,
                             InstructorMapper instructorMapper) {
        this.instructorRepository = instructorRepository;
        this.instructorMapper = instructorMapper;
    }

    public InstructorResponse getInstructorById(Integer id) {
        return instructorMapper.toResponse(instructorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + id + " not found")
        ));
    }

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

    @Transactional
    public void deleteInstructorById(Integer id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + id + " not found")
        );

        instructor.getCourses().forEach(course -> course.getInstructors().remove(instructor));
        instructorRepository.delete(instructor);
    }
}
