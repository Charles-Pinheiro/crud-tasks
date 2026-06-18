package com.project.tasks.application.validation.task.create;

import com.project.tasks.application.exception.BusinessException;
import com.project.tasks.application.exception.ErrorConstants;
import com.project.tasks.domain.enumeration.TaskPriority;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class HighPriorityRequiresDueDateValidationHandler extends CreateTaskValidationHandler {

    @Override
    protected void validateCurrent(CreateTaskValidationContext context) {
        BusinessException.throwIf(
                context.task().getPriority() == TaskPriority.HIGH && context.task().getDueDate() == null,
                ErrorConstants.HIGH_PRIORITY_TASK_REQUIRES_DUE_DATE,
                HttpStatus.BAD_REQUEST
        );
    }
}
