package com.example.store.aspect;

import com.example.store.exceptions.ErrorResponse;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.exceptions.StoreValueNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                "Something went wrong: " + ex.getMessage(),
                "GENERIC_ERROR",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StoreValueNotFound.class)
    public ResponseEntity<ErrorResponse> handleValueNotFoundException(StoreValueNotFound ex) {
        String message = ex.getMessage();
        ErrorResponse error = new ErrorResponse(message, "NOT_FOUND", LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StoreIllegalArgument.class)
    public ResponseEntity<ErrorResponse> illegalArgument(StoreIllegalArgument ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "VALIDATION_ERROR", LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
