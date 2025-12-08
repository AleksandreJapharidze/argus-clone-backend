package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.entities.Group;
import com.example.argusclone.entities.Student;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.exceptions.TooManyResourcesException;
import com.example.argusclone.mappers.GroupMapper;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.StudentGroupAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentGroupAssignmentServiceImpl implements StudentGroupAssignmentService {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final GroupMapper groupMapper;

    @Autowired
    public StudentGroupAssignmentServiceImpl(GroupRepository groupRepository,
                                             StudentRepository studentRepository,
                                             GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.groupMapper = groupMapper;
    }

    @Override
    public GroupResponse assignStudentToGroup(Integer groupId, Integer studentId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group " + groupId + " not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student " + studentId + " not found"));

        // 1. Check duplicate within this course
        if (groupRepository.countGroupsForStudentInCourse(group.getCourse().getId(), studentId) > 0) {
            throw new DuplicateResourceException("Student already in a group for this course");
        }

        // 2. Check 5 course limit
        if (studentRepository.countStudentGroups(studentId) >= 5) {
            throw new TooManyResourcesException("Student cannot join more than 5 groups");
        }

        // 3. Assign student
        group.getStudents().add(student);
        return groupMapper.toResponse(groupRepository.save(group));
    }
}
