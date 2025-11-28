package com.example.argusclone.mappers;

import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.dtos.lecture.LectureResponse;
import com.example.argusclone.entities.Lecture;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LectureMapper {
    Lecture toEntity(CreateLectureRequest request);

    LectureResponse toResponse(Lecture lecture);
}
