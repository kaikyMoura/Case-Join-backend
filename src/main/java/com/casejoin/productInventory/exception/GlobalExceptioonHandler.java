package com.casejoin.productInventory.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.casejoin.productInventory.dto.ErrorResponseDto;


@ControllerAdvice
public class GlobalExceptioonHandler {

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(CustomNotFoundException ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(404, "No result found", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(500, "Internal Server Error", "An unexpected error occurred");
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RequiredArgumentsMissing.class)
    public ResponseEntity<ErrorResponseDto> handleEntityRequiredArgumentsMissing(RequiredArgumentsMissing ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(401, "Required arguments missing", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}