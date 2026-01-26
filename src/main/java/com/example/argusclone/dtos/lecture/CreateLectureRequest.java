package com.example.argusclone.dtos.lecture;

import java.time.LocalTime;
import java.util.Objects;

public class CreateLectureRequest {
    private String dayOfWeek;
    private LocalTime lectureStartTime;
    private LocalTime lectureEndTime;
    private String roomNumber;

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CreateLectureRequest that = (CreateLectureRequest) o;
        return Objects.equals(dayOfWeek, that.dayOfWeek) &&
                Objects.equals(lectureStartTime, that.lectureStartTime) &&
                Objects.equals(lectureEndTime, that.lectureEndTime) &&
                Objects.equals(roomNumber, that.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, lectureStartTime, lectureEndTime, roomNumber);
    }
}
