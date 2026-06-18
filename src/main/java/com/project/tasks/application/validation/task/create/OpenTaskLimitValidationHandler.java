package com.project.tasks.application.validation.task.create;

import com.project.tasks.application.exception.BusinessException;
import com.project.tasks.application.exception.ErrorConstants;
import com.project.tasks.domain.enumeration.TaskStatus;
import com.project.tasks.domain.enumeration.UserRole;
import com.project.tasks.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(4)
@RequiredArgsConstructor
public class OpenTaskLimitValidationHandler extends CreateTaskValidationHandler {

    private static final List<TaskStatus> OPEN_STATUSES = List.of(
            TaskStatus.TODO,
            TaskStatus.IN_PROGRESS,
            TaskStatus.PAUSED
    );

    private final TaskRepository repository;

    @Value("${tasks.validation.max-open-tasks-per-user:10}")
    private int maxOpenTasksPerUser;

    @Override
    protected void validateCurrent(CreateTaskValidationContext context) {
        if (context.currentUser().getRole() == UserRole.ROLE_ADMIN) {
            return;
        }

        long openTasks = repository.countByOwnerIdAndStatusIn(
                context.currentUser().getId(),
                OPEN_STATUSES
        );

        BusinessException.throwIf(
                openTasks >= maxOpenTasksPerUser,
                ErrorConstants.OPEN_TASK_LIMIT_EXCEEDED,
                HttpStatus.BAD_REQUEST
        );
    }
}
