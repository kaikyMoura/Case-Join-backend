package com.testcase.join.dto;

public record ErrorResponseDto(int statusCode, String message, String details) {
}