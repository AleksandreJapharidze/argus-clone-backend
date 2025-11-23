package com.example.argusclone.dtos.group;

import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.dtos.student.CreateStudentRequest;

import java.util.List;

public class CreateGroupRequest {
    private List<CreateLectureRequest> lectures;
    private List<CreateStudentRequest> students;

    public List<CreateLectureRequest> getLectures() {
        return lectures;
    }

    public void setLectures(List<CreateLectureRequest> lectures) {
        this.lectures = lectures;
    }

    public List<CreateStudentRequest> getStudents() {
        return students;
    }

    public void setStudents(List<CreateStudentRequest> students) {
        this.students = students;
    }
}
