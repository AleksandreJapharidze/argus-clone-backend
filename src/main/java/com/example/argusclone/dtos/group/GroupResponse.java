package com.example.argusclone.dtos.group;

import com.example.argusclone.dtos.lecture.LectureResponse;

import java.util.List;

public class GroupResponse {
    private Integer id;
    private List<LectureResponse> lectures;

    public Integer getId() {
        return id;
    }

    public List<LectureResponse> getLectures() {
        return lectures;
    }
}
