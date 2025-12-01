package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.syllabus.SyllabusRequest;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Syllabus;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.SyllabusMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.SyllabusRepository;
import com.example.argusclone.services.SyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyllabusServiceImpl implements SyllabusService {
    private final SyllabusRepository syllabusRepository;
    private final CourseRepository courseRepository;
    private final SyllabusMapper syllabusMapper;

    @Autowired
    public SyllabusServiceImpl(SyllabusRepository syllabusRepository,
                               CourseRepository courseRepository,
                               SyllabusMapper syllabusMapper) {
        this.syllabusRepository = syllabusRepository;
        this.courseRepository = courseRepository;
        this.syllabusMapper = syllabusMapper;
    }

    @Override
    public SyllabusResponse getSyllabusByCourseId(Integer courseId) {
        Syllabus syllabus = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("Course with an id of " + courseId + " not found")
        ).getSyllabus();

        return syllabusMapper.toResponse(syllabus);
    }

    @Override
    public SyllabusResponse addSyllabusToCourse(Integer courseId, SyllabusRequest syllabus) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("Course with an id of " + courseId + " not found")
        );

        Syllabus newSyllabus = syllabusMapper.toEntity(syllabus);
        course.setSyllabus(newSyllabus);
        return syllabusMapper.toResponse(syllabusRepository.save(newSyllabus));
    }

    @Override
    public SyllabusResponse updatePrerequisitesByCourseId(Integer courseId, List<String> prerequisites) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("Course with an id of " + courseId + " not found")
        );

        if (course.getSyllabus() == null) {
            throw new ResourceNotFoundException("Syllabus for course with id " + courseId + " not found");
        }

        course.getSyllabus().setPrerequisites(prerequisites);
        return syllabusMapper.toResponse(syllabusRepository.save(course.getSyllabus()));
    }

    @Override
    public void deleteSyllabusByCourseId(Integer courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("Course with an id of " + courseId + " not found")
        );

        Syllabus syllabus = course.getSyllabus();
        if (syllabus != null) {
            course.setSyllabus(null);
            courseRepository.save(course);
            syllabusRepository.delete(syllabus);
        }
    }
}
