package com.example.argusclone.dtos.lecture;

import java.time.LocalDate;
import java.time.LocalTime;

public record LectureForDay(LocalDate lectureDate, LocalTime lectureStartTime, LocalTime lectureEndTime, String roomNumber, String courseName, String groupName) {
}
