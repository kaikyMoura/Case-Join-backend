package com.casejoin.productInventory.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.casejoin.productInventory.enums.Category;
import com.fasterxml.jackson.annotation.JsonInclude;

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
