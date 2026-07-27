package com.example.argusclone.entities.pending;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OrphanedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer courseId;
    private String fileName;

    public Integer getId() {
        return id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
