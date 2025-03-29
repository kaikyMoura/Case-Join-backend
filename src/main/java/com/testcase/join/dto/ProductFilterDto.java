package com.testcase.join.dto;

import java.util.Optional;

public record ProductFilterDto(
    Optional<String> name,
    Optional<String> category,
    Optional<String> brand,
    Optional<Double> minPrice,
    Optional<Double> maxPrice,
    Optional<Integer> limit,
    Optional<Integer> page,
    Optional<Integer> pageSize
) {}
