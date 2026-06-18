package com.project.tasks.application.exception;

import lombok.Getter;

@Getter
public enum ErrorConstants {

    TASK_NOT_FOUND("Task not found"),
    ;

    private final String message;

    ErrorConstants(String message) {
        this.message = message;
    }

}
