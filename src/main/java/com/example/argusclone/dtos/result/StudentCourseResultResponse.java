package com.example.argusclone.dtos.result;

public record StudentCourseResultResponse(Integer id, String courseName, String studentName, Boolean hasPassed, Integer finalGrade) {
}
