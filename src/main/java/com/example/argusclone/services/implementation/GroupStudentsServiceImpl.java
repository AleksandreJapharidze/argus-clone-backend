package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.entities.Group;
import com.example.argusclone.entities.Student;
import com.example.argusclone.exceptions.DuplicateResourceException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.exceptions.TooManyResourcesException;
import com.example.argusclone.mappers.GroupMapper;
import com.example.argusclone.mappers.StudentMapper;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.services.GroupStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupStudentsServiceImpl implements GroupStudentsService {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final GroupMapper groupMapper;
    private final StudentMapper studentMapper;

    @Autowired
    public GroupStudentsServiceImpl(GroupRepository groupRepository,
                                    StudentRepository studentRepository,
                                    GroupMapper groupMapper,
                                    StudentMapper studentMapper) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.groupMapper = groupMapper;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<StudentResponse> getStudentsByGroupIdAndCourseId(Integer groupId, Integer courseId) {
        List<Student> students = studentRepository.findByGroupIdAndCourseId(groupId, courseId);

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
