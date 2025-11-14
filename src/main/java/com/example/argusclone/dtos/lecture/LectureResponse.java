package com.example.argusclone.dtos.lecture;

import java.time.LocalDate;
import java.time.LocalTime;

public class LectureResponse {
    private Integer id;
    private LocalDate lectureDate;
    private LocalTime lectureStartTime;
    private LocalTime lectureEndTime;
    private String roomNumber;

    public Integer getId() {
        return id;
    }

    public LocalDate getLectureDate() {
        return lectureDate;
    }

    public LocalTime getLectureStartTime() {
        return lectureStartTime;
    }

    public LocalTime getLectureEndTime() {
        return lectureEndTime;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}
