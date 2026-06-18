package com.project.tasks.application.validation.task.create;

import com.project.tasks.application.exception.BusinessException;
import com.project.tasks.application.exception.ErrorConstants;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(1)
public class DueDateNotInPastValidationHandler extends CreateTaskValidationHandler {

    @Override
    protected void validateCurrent(CreateTaskValidationContext context) {
        BusinessException.throwIf(
                context.task().getDueDate() != null && context.task().getDueDate().isBefore(LocalDateTime.now()),
                ErrorConstants.TASK_DUE_DATE_IN_PAST,
                HttpStatus.BAD_REQUEST
        );
    }
}
