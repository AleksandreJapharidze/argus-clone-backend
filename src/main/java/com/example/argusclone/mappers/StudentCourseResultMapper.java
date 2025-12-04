package com.example.argusclone.mappers;

import com.example.argusclone.dtos.result.StudentCourseResultResponse;
import com.example.argusclone.entities.StudentCourseResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentCourseResultMapper {
    StudentCourseResultResponse toResponse(StudentCourseResult studentCourseResult);
}
