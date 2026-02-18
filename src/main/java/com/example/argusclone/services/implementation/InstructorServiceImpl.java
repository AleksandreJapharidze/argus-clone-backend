package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.user.User;
import com.example.argusclone.dtos.instructor.CreateInstructorRequest;
import com.example.argusclone.dtos.instructor.InstructorResponse;
import com.example.argusclone.entities.Instructor;
import com.example.argusclone.events.eventclasses.UserCreationEvent;
import com.example.argusclone.events.eventclasses.UserDeletionEvent;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.helpers.RandomPasswordGenerator;
import com.example.argusclone.helpers.UserDataSaver;
import com.example.argusclone.mappers.InstructorMapper;
import com.example.argusclone.repositories.InstructorRepository;
import com.example.argusclone.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository,
                                 InstructorMapper instructorMapper,
                                 ApplicationEventPublisher applicationEventPublisher) {
        this.instructorRepository = instructorRepository;
        this.instructorMapper = instructorMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Cacheable(value = "INSTRUCTOR_CACHE", key = "'id: ' + #id")
    public InstructorResponse getInstructorById(Integer id) {
        return instructorMapper.toResponse(instructorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + id + " not found")
        ));
    }

    @Override
    @Transactional
    @CachePut(value = "INSTRUCTOR_CACHE", key = "'id: ' + #result.getId()")
    public InstructorResponse addInstructor(CreateInstructorRequest instructor) {
        instructorRepository.findByEmail(instructor.getEmail()).ifPresent(i -> {
            throw new DuplicateResourceException("Instructor with email " + instructor.getEmail() + " already exists");
        });

        Instructor newInstructor = instructorMapper.toEntity(instructor);
        Instructor savedInstructor = instructorRepository.save(newInstructor);

        String password = RandomPasswordGenerator.generateRandomPassword(8);

        UserDataSaver.saveUser(new User(instructor.getEmail(), password, "Instructor"));

        applicationEventPublisher.publishEvent(
                new UserCreationEvent(savedInstructor.getUuid(), savedInstructor.getEmail(), password, "INSTRUCTOR")
        );

        return instructorMapper.toResponse(savedInstructor);
    }

    @Override
    @Transactional
    @CacheEvict(value = "INSTRUCTOR_CACHE", key = "'id: ' + #id")
    public void deleteInstructorById(Integer id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Instructor with an id of " + id + " not found")
        );

        String instructorEmail = instructor.getEmail();

        instructor.getCourses().forEach(course -> course.getInstructors().remove(instructor));
        instructorRepository.delete(instructor);

        UserDataSaver.deleteUser(instructorEmail);

        applicationEventPublisher.publishEvent(
                new UserDeletionEvent(instructorEmail)
        );
    }
}
