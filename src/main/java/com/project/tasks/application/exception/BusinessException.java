package com.project.tasks.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorConstants error;

    public BusinessException(ErrorConstants error, HttpStatus status) {
        super(error.getMessage());
        this.error = error;
        this.status = status;
    }

    public BusinessException(Class<?> entityClass) {
        super(entityClass.getSimpleName() + " not found");
        this.error = null;
        this.status = HttpStatus.NOT_FOUND;
    }

    public static Supplier<BusinessException> unauthorized(ErrorConstants error) {
        return () -> new BusinessException(error, HttpStatus.UNAUTHORIZED);
    }

    public static Supplier<BusinessException> forbidden(ErrorConstants error) {
        return () -> new BusinessException(error, HttpStatus.FORBIDDEN);
    }

    public static Supplier<BusinessException> notFound(ErrorConstants error) {
        return () -> new BusinessException(error, HttpStatus.NOT_FOUND);
    }

    public static Supplier<BusinessException> notFound(Class<?> entityClass) {
        return () -> new BusinessException(entityClass);
    }

}
