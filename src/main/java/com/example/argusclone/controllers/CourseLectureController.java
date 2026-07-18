package com.example.argusclone.controllers;

import com.example.argusclone.dtos.lecture.LectureForDay;
import com.example.argusclone.exceptions.NotAMemberException;
import com.example.argusclone.services.CourseInstructorService;
import com.example.argusclone.services.LecturesForDayGetterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/all-groups/lectures")
public class CourseLectureController {
    private final LecturesForDayGetterService lecturesForDayGetterService;
    private final CourseInstructorService courseInstructorService;

    public CourseLectureController(LecturesForDayGetterService lecturesForDayGetterService,
                                   CourseInstructorService courseInstructorService) {
        this.lecturesForDayGetterService = lecturesForDayGetterService;
        this.courseInstructorService = courseInstructorService;
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping(params = {"instructorId", "lectureDate"})
    public ResponseEntity<Iterable<LectureForDay>> getLecturesForDayForInstructor(@PathVariable Integer courseId,
                                                                                  @RequestParam Integer instructorId,
                                                                                  @RequestParam LocalDate lectureDate,
                                                                                  @AuthenticationPrincipal Jwt jwt) {
        Long instructorIdFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
        if (instructorIdFromToken == null || !instructorId.equals(instructorIdFromToken.intValue())) {
            return ResponseEntity.status(403).body(null);
        }

        List<Integer> instructorCourseIds = courseInstructorService.getCourseIdsByInstructorId(instructorId);
        if (!instructorCourseIds.contains(courseId)) {
            throw new NotAMemberException("The instructor is not instructor of the course with id " + courseId);
        }

        return ResponseEntity.ok(lecturesForDayGetterService.getLecturesForDayForInstructor(courseId, instructorId, lectureDate));
    }
}
