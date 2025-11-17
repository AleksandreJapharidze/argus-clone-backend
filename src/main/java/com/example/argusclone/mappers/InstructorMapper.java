package com.example.argusclone.mappers;

import com.example.argusclone.dtos.instructor.CreateInstructorRequest;
import com.example.argusclone.dtos.instructor.InstructorResponse;
import com.example.argusclone.entities.Instructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstructorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Instructor toEntity(CreateInstructorRequest request);

//    @Mapping(target = "courses", ignore = true)
    InstructorResponse toResponse(Instructor instructor);
}
