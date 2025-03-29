package com.casejoin.productInventory.dto;

public record ApiResponseDto<T>(
        int statusCode,
        String message,
        T data) {
}
