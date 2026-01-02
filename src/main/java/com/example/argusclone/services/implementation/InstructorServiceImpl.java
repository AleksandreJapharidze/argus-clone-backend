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
    public InstructorResponse getInstructorById(Integer id) {
        return instructorMapper.toResponse(instructorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + id + " not found")
        ));
    }

    @Override
    public InstructorResponse getInstructorByName(String name) {
        return instructorMapper.toResponse(instructorRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with a name of " + name + " not found")
        ));
    }

    @Override
    public InstructorResponse getInstructorByEmail(String email) {
        return instructorMapper.toResponse(instructorRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an email of " + email + " not found")
        ));
    }

    @Override
    public InstructorResponse addInstructor(CreateInstructorRequest instructor) {
        instructorRepository.findByEmail(instructor.getEmail()).ifPresent(i -> {
            throw new DuplicateResourceException("Instructor with email " + instructor.getEmail() + " already exists");
        });

        Instructor newInstructor = instructorMapper.toEntity(instructor);
        return instructorMapper.toResponse(instructorRepository.save(newInstructor));
    }

    @Override
    public void deleteInstructorById(Integer id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + id + " not found")
        );

        instructor.getCourses().forEach(course -> course.getInstructors().remove(instructor));
        instructorRepository.deleteById(id);
    }
}
