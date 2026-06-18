package com.project.tasks.application.exception;

import lombok.Getter;

@Getter
public enum ErrorConstants {

    UNAUTHENTICATED("Unauthenticated user"),
    INVALID_USER("Invalid authenticated user"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    ;

    private final String message;

    ErrorConstants(String message) {
        this.message = message;
    }

}
