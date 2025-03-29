package com.testcase.join.dto;

import java.util.Optional;
import java.util.UUID;

import com.testcase.join.enums.Category;

import jakarta.validation.constraints.NotBlank;

@NotBlank
public record ProductDto(
        Optional<UUID> id,
        String name,
        Optional<String> description,
        String brand,
        Category category,
        int quantity,
        double price) {
}
