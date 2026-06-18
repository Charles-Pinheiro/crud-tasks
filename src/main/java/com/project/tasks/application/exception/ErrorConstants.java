package com.project.tasks.application.exception;

import lombok.Getter;

@Getter
public enum ErrorConstants {

    UNAUTHENTICATED("Unauthenticated user"),
    INVALID_USER("Invalid authenticated user"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    TASK_DUE_DATE_IN_PAST("Task due date cannot be in the past"),
    OPEN_TASK_LIMIT_EXCEEDED("Open task limit exceeded"),
    TASK_TITLE_ALREADY_EXISTS("Task title already exists for this user"),
    HIGH_PRIORITY_TASK_REQUIRES_DUE_DATE("High priority task requires due date"),
    ;

    private final String message;

    ErrorConstants(String message) {
        this.message = message;
    }

}
