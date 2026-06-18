package com.project.tasks.application.validation.task.create;

import com.project.tasks.application.exception.BusinessException;
import com.project.tasks.application.exception.ErrorConstants;
import com.project.tasks.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@RequiredArgsConstructor
public class UniqueTitleForOwnerValidationHandler extends CreateTaskValidationHandler {

    private final TaskRepository repository;

    @Override
    protected void validateCurrent(CreateTaskValidationContext context) {
        BusinessException.throwIf(
                repository.existsByOwnerIdAndTitleIgnoreCase(
                        context.currentUser().getId(),
                        context.task().getTitle()
                ),
                ErrorConstants.TASK_TITLE_ALREADY_EXISTS,
                HttpStatus.CONFLICT
        );
    }
}
