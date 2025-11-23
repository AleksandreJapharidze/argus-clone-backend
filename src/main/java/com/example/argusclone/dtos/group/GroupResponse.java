package com.example.argusclone.dtos.group;

import com.example.argusclone.dtos.lecture.LectureResponse;
import com.example.argusclone.dtos.student.StudentResponse;

import java.util.List;

public class GroupResponse {
    private Integer id;
    private List<LectureResponse> lectures;
    private List<StudentResponse> students;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<LectureResponse> getLectures() {
        return lectures;
    }

    public void setLectures(List<LectureResponse> lectures) {
        this.lectures = lectures;
    }

    public List<StudentResponse> getStudents() {
        return students;
    }

    public void setStudents(List<StudentResponse> students) {
        this.students = students;
    }
}
