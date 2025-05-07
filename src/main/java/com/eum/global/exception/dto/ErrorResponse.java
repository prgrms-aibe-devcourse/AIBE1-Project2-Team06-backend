package com.eum.global.exception.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        String detail
) {
    public static ErrorResponse of(HttpStatus status, String message, String path, String detail) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                detail
        );
    }

    public static ErrorResponse of(HttpStatus status, String message, String path) {
        return of(status, message, path, null);
    }
}
