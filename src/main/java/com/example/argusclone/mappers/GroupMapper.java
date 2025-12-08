package com.example.argusclone.mappers;

import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.entities.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {LectureMapper.class}
)
public interface GroupMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "lectures", ignore = true)
    @Mapping(target = "students", ignore = true)

    Group toEntity(CreateGroupRequest request);

    GroupResponse toResponse(Group group);
}
