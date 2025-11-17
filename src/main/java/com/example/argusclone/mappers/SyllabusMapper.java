package com.example.argusclone.mappers;

import com.example.argusclone.dtos.syllabus.SyllabusRequest;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;
import com.example.argusclone.entities.Syllabus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SyllabusMapper {
    @Mapping(target = "id", ignore = true)
    Syllabus toEntity(SyllabusRequest request);

    SyllabusResponse toResponse(Syllabus syllabus);
}
