package com.example.argusclone.mappers;

import com.example.argusclone.dtos.material.MaterialResponse;
import com.example.argusclone.entities.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaterialMapper {
    MaterialResponse toResponse(Material material);
}
