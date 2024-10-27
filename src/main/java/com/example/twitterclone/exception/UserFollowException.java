package com.example.twitterclone.exception;

public class UserFollowException extends RuntimeException {
    private final String errorCode;

    public UserFollowException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
