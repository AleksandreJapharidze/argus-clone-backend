package com.example.argusclone.controllers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.course.CreateCourseRequest;
import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.dtos.syllabus.SyllabusRequest;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;
import com.example.argusclone.services.CourseService;
import com.example.argusclone.services.GroupService;
import com.example.argusclone.services.InstructorCourseAssignmentService;
import com.example.argusclone.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService courseService;
    private final GroupService groupService;
    private final ScoreService scoreService;
    private final InstructorCourseAssignmentService instructorCourseAssignmentService;

    @Autowired
    public CourseController(CourseService courseService, GroupService groupService, ScoreService scoreService,
                            InstructorCourseAssignmentService instructorCourseAssignmentService) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.scoreService = scoreService;
        this.instructorCourseAssignmentService = instructorCourseAssignmentService;
    }

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

    @GetMapping("/{courseId}/syllabus")
    public ResponseEntity<SyllabusResponse> getCourseSyllabus(@PathVariable Integer courseId) {
        return ResponseEntity.ok(courseService.getCourseSyllabus(courseId));
    }

    @GetMapping("/{courseId}/groups")
    public ResponseEntity<Iterable<GroupResponse>> getGroupsForCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(groupService.getGroupsForCourse(courseId));
    }

    @GetMapping("/{courseId}/students/{studentId}/scores")
    public ResponseEntity<Iterable<ScoreResponse>> getStudentScoresByCourseId(@PathVariable Integer courseId,
                                                                               @PathVariable Integer studentId) {
        return ResponseEntity.ok(scoreService.getStudentScoresByCourseId(courseId, studentId));
    }

    @PostMapping
    public ResponseEntity<CourseResponse> addCourse(@RequestBody CreateCourseRequest course) {
        CourseResponse savedCourse = courseService.addCourse(course);

        URI location = URI.create("/api/v1/courses/" + savedCourse.getId());
        return ResponseEntity.created(location).body(savedCourse);
    }

    @PostMapping("/{courseId}/groups")
    public ResponseEntity<GroupResponse> createGroup(@PathVariable Integer courseId, CreateGroupRequest group) {
        GroupResponse savedGroup = groupService.createGroup(courseId, group);

        URI location = URI.create("/api/v1/groups/" + savedGroup.getId());
        return ResponseEntity.created(location).body(savedGroup);
    }

    @PostMapping("/{courseId}/syllabus")
    public ResponseEntity<SyllabusResponse> addCourseSyllabus(@PathVariable Integer courseId,
                                                              @RequestBody SyllabusRequest syllabus) {
        SyllabusResponse savedSyllabus = courseService.addCourseSyllabus(courseId, syllabus);

        URI location = URI.create("/api/v1/courses/" + courseId + "/syllabus");
        return ResponseEntity.created(location).body(savedSyllabus);
    }

    @PostMapping("/{courseId}/scores")
    public ResponseEntity<List<ScoreResponse>> generateEmptyListOfScoresForStudentsByCourseId(@PathVariable Integer courseId,
                                                                                              @RequestBody List<CreateScoreRequest> scores) {
        List<ScoreResponse> emptyScoresList = scoreService.generateEmptyListsOfScoresForStudentsByCourseId(courseId, scores);
        return ResponseEntity.ok(emptyScoresList);
    }

    @PatchMapping("/{courseId}/syllabus")
    public ResponseEntity<SyllabusResponse> updateSyllabusPrerequisites(@PathVariable Integer courseId,
                                                                        @RequestBody List<String> prerequisites) {
        return ResponseEntity.ok(courseService.updateSyllabusPrerequisites(courseId, prerequisites));
    }

    @PatchMapping("/{courseId}/instructors/{instructorId}")
    public ResponseEntity<CourseResponse> assignInstructorToCourse(@PathVariable Integer courseId,
                                                                   @PathVariable Integer instructorId) {
        return ResponseEntity.ok(instructorCourseAssignmentService.assignInstructorToCourse(courseId, instructorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseById(@PathVariable Integer id) {
        groupService.deleteLecturesByCourseId(id);
        scoreService.deleteScoresByCourseId(id);
        groupService.deleteGroupsByCourseId(id);
        courseService.deleteCourseSyllabus(id);
        courseService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{courseId}/syllabus")
    public ResponseEntity<Void> deleteCourseSyllabus(@PathVariable Integer courseId) {
        courseService.deleteCourseSyllabus(courseId);
        return ResponseEntity.noContent().build();
    }
}
