package com.example.argusclone.mappers;

import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "scores", ignore = true)
    Student toEntity(CreateStudentRequest request);

    StudentResponse toResponse(Student student);
}
