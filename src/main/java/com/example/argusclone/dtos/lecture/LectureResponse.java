package com.example.argusclone.dtos.lecture;

import java.time.LocalDate;
import java.time.LocalTime;

public record LectureResponse(Integer id, LocalDate lectureDate, LocalTime lectureStartTime, LocalTime lectureEndTime, String roomNumber) {
}
