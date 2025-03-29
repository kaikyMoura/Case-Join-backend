package com.testcase.join.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.testcase.join.enums.Category;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDto(
                UUID id,
                String name,
                String description,
                String brand,
                Category category,
                Integer quantity,
                Double price,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}
