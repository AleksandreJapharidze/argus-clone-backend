package com.example.argusclone.dtos.material;

import java.time.LocalDate;

public record MaterialResponse(Integer id, String objectKey, String originalName, String contentType, Integer size, LocalDate createdAt) {
}
