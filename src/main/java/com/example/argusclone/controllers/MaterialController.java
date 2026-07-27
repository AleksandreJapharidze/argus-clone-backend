package com.example.argusclone.controllers;

import com.example.argusclone.dtos.material.MaterialResponse;
import com.example.argusclone.exceptions.FileUploadException;
import com.example.argusclone.services.CourseInstructorService;
import com.example.argusclone.services.CourseStudentService;
import com.example.argusclone.services.MaterialService;
import com.example.argusclone.services.StorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/materials")
public class MaterialController {
    private final StorageService storageService;
    private final MaterialService materialService;
    private final CourseInstructorService courseInstructorService;
    private final CourseStudentService courseStudentService;

    public MaterialController(StorageService storageService,
                              MaterialService materialService,
                              CourseInstructorService courseInstructorService,
                              CourseStudentService courseStudentService) {
        this.storageService = storageService;
        this.materialService = materialService;
        this.courseInstructorService = courseInstructorService;
        this.courseStudentService = courseStudentService;
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('STUDENT')")
    @GetMapping
    public ResponseEntity<List<MaterialResponse>> getAllMaterialMetadatasByCourseId(@PathVariable Integer courseId,
                                                                                    @AuthenticationPrincipal Jwt jwt) {
        Long idFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
        String role = jwt.getClaimAsString("role");
        if ("STUDENT".equals(role)) {
            if (idFromToken == null) {
                return ResponseEntity.status(403).body(null);
            }

            List<Integer> courseIds = courseStudentService.getCourseIdsByStudentId(idFromToken.intValue());
            if (!courseIds.contains(courseId)) {
                return ResponseEntity.status(403).body(null);
            }
        } else {
            List<Integer> instructorIds = courseInstructorService.getInstructorIdsByCourseId(courseId);
            if (idFromToken == null || !instructorIds.contains(idFromToken.intValue())) {
                return ResponseEntity.status(403).body(null);
            }
        }

        return ResponseEntity.ok(materialService.getAllMaterialsByCourseId(courseId));
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('STUDENT')")
    @GetMapping(params = "fileName")
    public ResponseEntity<byte[]> getMaterialFile(@PathVariable Integer courseId,
                                                  @RequestParam String fileName,
                                                  @AuthenticationPrincipal Jwt jwt) {
        Long idFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
        String role = jwt.getClaimAsString("role");
        if ("STUDENT".equals(role)) {
            if (idFromToken == null) {
                return ResponseEntity.status(403).body(null);
            }

            List<Integer> courseIds = courseStudentService.getCourseIdsByStudentId(idFromToken.intValue());
            if (!courseIds.contains(courseId)) {
                return ResponseEntity.status(403).body(null);
            }
        } else {
            List<Integer> instructorIds = courseInstructorService.getInstructorIdsByCourseId(courseId);
            if (idFromToken == null || !instructorIds.contains(idFromToken.intValue())) {
                return ResponseEntity.status(403).body(null);
            }
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(storageService.downloadFile(courseId, fileName)
        );
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<MaterialResponse> uploadMaterialFile(@PathVariable Integer courseId,
                                                               @RequestParam("file") MultipartFile file,
                                                               @AuthenticationPrincipal Jwt jwt) {
        Long instructorIdFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
        List<Integer> instructorIds = courseInstructorService.getInstructorIdsByCourseId(courseId);
        if (instructorIdFromToken == null || !instructorIds.contains(instructorIdFromToken.intValue())) {
            return ResponseEntity.status(403).body(null);
        }

        if (file.isEmpty()) {
            throw new FileUploadException("File is empty!");
        }

        return ResponseEntity.ok(materialService.addNewMaterial(courseId, file));
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/{materialId}")
    public ResponseEntity<Void> deleteMaterialById(@PathVariable Integer courseId,
                                                   @PathVariable Integer materialId,
                                                   @AuthenticationPrincipal Jwt jwt) {
        Long instructorIdFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
        List<Integer> instructorIds = courseInstructorService.getInstructorIdsByCourseId(courseId);
        if (instructorIdFromToken == null || !instructorIds.contains(instructorIdFromToken.intValue())) {
            return ResponseEntity.status(403).body(null);
        }

        materialService.deleteMaterialById(courseId, materialId);
        return ResponseEntity.noContent().build();
    }
}
