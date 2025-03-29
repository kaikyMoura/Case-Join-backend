package com.casejoin.productInventory.dto;

public record ErrorResponseDto(int statusCode, String message, String details) {
}