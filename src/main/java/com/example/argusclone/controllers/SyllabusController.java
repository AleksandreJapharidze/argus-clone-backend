package com.example.argusclone.controllers;

import com.example.argusclone.dtos.syllabus.SyllabusRequest;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;
import com.example.argusclone.entities.embeddable.GradingWeight;
import com.example.argusclone.entities.embeddable.Prerequisite;
import com.example.argusclone.services.SyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/syllabus")
public class SyllabusController {
    private final SyllabusService syllabusService;

    @Autowired
    public SyllabusController(SyllabusService syllabusService) {
        this.syllabusService = syllabusService;
    }

    @GetMapping
    public ResponseEntity<SyllabusResponse> getSyllabusByCourseId(@PathVariable Integer courseId) {
        return ResponseEntity.ok(syllabusService.getSyllabusByCourseId(courseId));
    }

    @PostMapping
    public ResponseEntity<SyllabusResponse> addSyllabusToCourse(@PathVariable Integer courseId,
                                                                @RequestBody SyllabusRequest syllabus) {
        SyllabusResponse savedSyllabus = syllabusService.addSyllabusToCourse(courseId, syllabus);

        URI location = URI.create("/api/v1/courses/" + courseId + "/syllabus");
        return ResponseEntity.created(location).body(savedSyllabus);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/prerequisites")
    public ResponseEntity<SyllabusResponse> updateCourseSyllabusPrerequisites(@PathVariable Integer courseId,
                                                                              @RequestBody Set<Prerequisite> prerequisites) {
        return ResponseEntity.ok(syllabusService.updatePrerequisitesByCourseId(courseId, prerequisites));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/grading-weights")
    public ResponseEntity<SyllabusResponse> updateCourseSyllabusGradingWeights(@PathVariable Integer courseId,
                                                                               @RequestBody List<GradingWeight> gradingWeights) {
        return ResponseEntity.ok(syllabusService.updateGradingWeightsByCourseId(courseId, gradingWeights));
    }
}
