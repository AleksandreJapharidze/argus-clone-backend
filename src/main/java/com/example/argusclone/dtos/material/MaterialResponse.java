package com.example.argusclone.dtos.material;

import java.time.LocalDate;

public record MaterialResponse(Integer id, String fileName, String contentType, Integer size, LocalDate createdAt) {
}
