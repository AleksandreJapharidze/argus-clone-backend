package com.example.argusclone.dtos.group;

import com.example.argusclone.dtos.lecture.CreateLectureRequest;

import java.util.List;

public class CreateGroupRequest {
    private List<CreateLectureRequest> lectures;

    public List<CreateLectureRequest> getLectures() {
        return lectures;
    }

    public void setLectures(List<CreateLectureRequest> lectures) {
        this.lectures = lectures;
    }
}
