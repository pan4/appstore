package com.dataart.apanch.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApiError {

    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private List<String> errors;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    ApiError(final HttpStatus status, final String message) {
        this();
        this.status = status;
        this.message = message;
    }

    ApiError(final HttpStatus status, final String message, final String error) {
        this();
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }

    ApiError(final HttpStatus status, final String message, final List<String> errors) {
        this();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
}
