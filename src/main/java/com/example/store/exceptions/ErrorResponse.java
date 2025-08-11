package com.example.store.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private String code;
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "ErrorResponse{" + "message='"
                + message + '\'' + ", code='"
                + code + '\'' + ", timestamp="
                + timestamp + '}';
    }
}
