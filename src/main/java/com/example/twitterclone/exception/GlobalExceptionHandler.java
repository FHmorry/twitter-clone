package com.example.twitterclone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserFollowException.class)
    public ResponseEntity<Map<String, String>> handleUserFollowException(UserFollowException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("errorCode", e.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
