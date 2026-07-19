package com.example.argusclone.services;

import com.example.argusclone.dtos.result.StudentCourseResultResponse;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;
import com.example.argusclone.entities.Group;
import com.example.argusclone.entities.Student;
import com.example.argusclone.entities.embeddable.Prerequisite;
import com.example.argusclone.exceptions.*;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.repositories.StudentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupStudentsService {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final SyllabusService syllabusService;
    private final StudentMapper studentMapper;

    public GroupStudentsService(GroupRepository groupRepository,
                                StudentRepository studentRepository,
                                StudentService studentService,
                                SyllabusService syllabusService,
                                StudentMapper studentMapper) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.syllabusService = syllabusService;
        this.studentMapper = studentMapper;
    }

    @Cacheable(cacheNames = "group-students-cache", key = "#groupId")
    public List<StudentResponse> getStudentsByGroupId(Integer groupId) {
        List<Student> students = studentRepository.findByGroupId(groupId);

        return students.stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Cacheable(cacheNames = "student-group-ids-cache", key = "#studentId")
    public List<Integer> getAllGroupIdsByStudentId(Integer studentId) {
        return groupRepository.findAllGroupIdsByStudentId(studentId);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "group-students-cache", key = "#groupId"),
            @CacheEvict(cacheNames = "student-group-ids-cache", key = "#studentId")
    })
    @Transactional
    public void assignStudentToGroup(Integer groupId, Integer studentId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group " + groupId + " not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student " + studentId + " not found"));

        checkPrerequisites(group, student);

        // 1. Check duplicate within this course
        if (groupRepository.countGroupsForStudentInCourse(group.getCourse().getId(), studentId) > 0) {
            throw new DuplicateResourceException("Student already in a group for this course");
        }

        // 2. Check the 5-course limit
        if (studentRepository.countStudentGroups(studentId) >= 5) {
            throw new TooManyResourcesException("Student cannot join more than 5 groups");
        }

        // 3. Assign student
        group.getStudents().add(student);
        groupRepository.save(group);
    }

    private void checkPrerequisites(Group group, Student student) {
        SyllabusResponse syllabus = syllabusService.getSyllabusByCourseId(group.getCourse().getId());
        if (syllabus == null) {
            throw new OperationNotAllowedYetException("Could not assign student to group: syllabus not created yet.");
        }

        Set<String> prerequisites = syllabus.prerequisites().stream()
                .map(Prerequisite::getPrerequisite)
                .collect(Collectors.toSet());
        if (prerequisites == null || prerequisites.isEmpty()) {
            return;
        }

        List<StudentCourseResultResponse> studentCourseResults = studentService.getStudentCoursesResultsByStudentId(student.getId());
        if (studentCourseResults == null || studentCourseResults.isEmpty()) {
            throw new PrerequisitesNotMetException("Could not assign student to group: prerequisites not met.");
        }

        Set<String> studentPassedCourses = studentCourseResults.stream()
                .filter(result -> result.hasPassed() == true)
                .map(StudentCourseResultResponse::courseName)
                .collect(Collectors.toSet());
        if (!studentPassedCourses.containsAll(prerequisites)) {
            throw new PrerequisitesNotMetException("Could not assign student to group: prerequisites not met.");
        }
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "group-students-cache", key = "#groupId"),
            @CacheEvict(cacheNames = "student-group-ids-cache", key = "#studentId")
    })
    public void removeStudentFromGroup(Integer groupId, Integer studentId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new ResourceNotFoundException("Group " + groupId + " not found")
        );

        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student " + studentId + " not found")
        );

        if (!group.getStudents().contains(student)) {
            throw new ResourceNotFoundException("Student not in group");
        }

        group.getStudents().remove(student);

        groupRepository.save(group);
    }
}
