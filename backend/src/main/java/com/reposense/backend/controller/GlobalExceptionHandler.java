package com.reposense.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(
            IllegalArgumentException ex) {

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "error", "Bad Request",
                        "message", ex.getMessage()
                ));
    }
@ExceptionHandler(HttpClientErrorException.class)
public ResponseEntity<Map<String, String>> handleGitHubError(
        HttpClientErrorException ex) {

    HttpStatusCode status = ex.getStatusCode();

    return ResponseEntity.status(status).body(
        Map.of(
            "error", "GitHub API Error",
            "message", ex.getResponseBodyAsString()
        )
    );
}

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, String>> handleApiConnection(
            RestClientException ex) {

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of(
                        "error", "GitHub Connection Error",
                        "message", "Unable to fetch data from GitHub."
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralError(
            Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Internal Server Error",
                        "message", "Something went wrong while analyzing repository."
                ));
    }
}