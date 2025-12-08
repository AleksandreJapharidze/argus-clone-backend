package com.example.argusclone.mappers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.course.CreateCourseRequest;
import com.example.argusclone.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {GroupMapper.class, InstructorMapper.class, SyllabusMapper.class}
)
public interface CourseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "syllabus", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "instructors", ignore = true)
    @Mapping(target = "scores", ignore = true)
    Course toEntity(CreateCourseRequest request);

    CourseResponse toResponse(Course course);
}
