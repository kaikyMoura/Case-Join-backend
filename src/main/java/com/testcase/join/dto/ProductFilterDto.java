package com.testcase.join.dto;

public record ProductFilterDto(
    String name,
    String category,
    String brand,
    Double minPrice,
    Double maxPrice,
    Integer page,
    Integer pageSize
) {}
