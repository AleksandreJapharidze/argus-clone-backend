package com.example.argusclone.mappers;

import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.dtos.lecture.LectureResponse;
import com.example.argusclone.entities.Lecture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LectureMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    Lecture toEntity(CreateLectureRequest request);

    LectureResponse toResponse(Lecture lecture);
}
