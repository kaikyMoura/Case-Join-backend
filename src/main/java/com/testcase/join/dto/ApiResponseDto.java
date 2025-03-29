package com.testcase.join.dto;

public record ApiResponseDto<T>(
        int statusCode,
        String message,
        T object) {
}
