package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.instructor.CreateInstructorRequest;
import com.example.argusclone.dtos.instructor.InstructorResponse;
import com.example.argusclone.entities.Instructor;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.InstructorMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.InstructorRepository;
import com.example.argusclone.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstructorServiceImpl implements InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private InstructorMapper instructorMapper;

    @Override
    public InstructorResponse getInstructorById(Integer id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + id + " not found")
        );
        return instructorMapper.toResponse(instructor);
    }

    @Override
    public InstructorResponse getInstructorByName(String name) {
        Instructor instructor = instructorRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with a name of " + name + " not found")
        );
        return instructorMapper.toResponse(instructor);
    }

    @Override
    public InstructorResponse getInstructorByEmail(String email) {
        Instructor instructor = instructorRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an email of " + email + " not found")
        );
        return instructorMapper.toResponse(instructor);
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
