package com.example.argusclone.controllers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.services.CourseService;
import com.example.argusclone.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private GroupService groupService;

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping(params = "name")
    public ResponseEntity<CourseResponse> getCourseByName(@RequestParam String courseName) {
        return ResponseEntity.ok(courseService.getCourseByName(courseName));
    }

    @GetMapping(params = "code")
    public ResponseEntity<CourseResponse> getCourseByCourseCode(@RequestParam String courseCode) {
        return ResponseEntity.ok(courseService.getCourseByCourseCode(courseCode));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{courseId}/groups")
    public ResponseEntity<Iterable<GroupResponse>> getGroupsForCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(groupService.getGroupsForCourse(courseId));
    }

    @PostMapping("/{courseId}/groups")
    public ResponseEntity<GroupResponse> createGroup(@PathVariable Integer courseId, CreateGroupRequest group) {
        GroupResponse savedGroup = groupService.createGroup(courseId, group);

        URI location = URI.create("/api/v1/groups/" + savedGroup.getId());
        return ResponseEntity.created(location).body(savedGroup);
    }

    @PatchMapping("/{courseId}/assign-instructor/{instructorId}")
    public ResponseEntity<CourseResponse> assignInstructorToCourse(@PathVariable Integer courseId, @PathVariable Integer instructorId) {
        return ResponseEntity.ok(courseService.assignInstructorToCourse(courseId, instructorId));
    }
}
