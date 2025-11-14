package com.example.argusclone.dtos.lecture;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateLectureRequest {
    private LocalDate lectureDate;
    private LocalTime lectureStartTime;
    private LocalTime lectureEndTime;
    private String roomNumber;

    public LocalDate getLectureDate() {
        return lectureDate;
    }

    public void setLectureDate(LocalDate lectureDate) {
        this.lectureDate = lectureDate;
    }

    public LocalTime getLectureStartTime() {
        return lectureStartTime;
    }

    public void setLectureStartTime(LocalTime lectureStartTime) {
        this.lectureStartTime = lectureStartTime;
    }

    public LocalTime getLectureEndTime() {
        return lectureEndTime;
    }

    public void setLectureEndTime(LocalTime lectureEndTime) {
        this.lectureEndTime = lectureEndTime;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
