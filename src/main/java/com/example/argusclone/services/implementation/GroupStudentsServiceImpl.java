package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.entities.Group;
import com.example.argusclone.entities.Student;
import com.example.argusclone.entities.StudentCourseResult;
import com.example.argusclone.entities.Syllabus;
import com.example.argusclone.entities.embeddable.Prerequisite;
import com.example.argusclone.exceptions.*;
import com.example.argusclone.mappers.GroupMapper;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.GroupStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupStudentsServiceImpl implements GroupStudentsService {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final GroupMapper groupMapper;
    private final StudentMapper studentMapper;

    @Autowired
    public GroupStudentsServiceImpl(GroupRepository groupRepository, StudentRepository studentRepository,
                                    GroupMapper groupMapper, StudentMapper studentMapper) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.groupMapper = groupMapper;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<StudentResponse> getStudentsByGroupId(Integer groupId) {
        List<Student> students = studentRepository.findByGroupId(groupId);

        return students.stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public GroupResponse assignStudentToGroup(Integer groupId, Integer studentId) {
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

        return groupMapper.toResponse(groupRepository.save(group));
    }

    private void checkPrerequisites(Group group, Student student) {
        Syllabus syllabus = group.getCourse().getSyllabus();
        if (syllabus == null) {
            throw new OperationNotAllowedYetException("Could not assign student to group: syllabus not created yet.");
        }

        Set<String> prerequisites = syllabus.getPrerequisites().stream()
                .map(Prerequisite::getPrerequisite)
                .collect(Collectors.toSet());
        if (prerequisites == null || prerequisites.isEmpty()) {
            return;
        }

        Set<StudentCourseResult> studentCourseResults = student.getStudentCourseResults();
        if (studentCourseResults == null || studentCourseResults.isEmpty()) {
            throw new PrerequisitesNotMetException("Could not assign student to group: prerequisites not met.");
        }

        Set<String> studentPassedCourses = studentCourseResults.stream()
                .filter(result -> result.getHasPassed() == true)
                .map(StudentCourseResult::getCourseName)
                .collect(Collectors.toSet());
        if (!studentPassedCourses.containsAll(prerequisites)) {
            throw new PrerequisitesNotMetException("Could not assign student to group: prerequisites not met.");
        }
    }

    @Override
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
