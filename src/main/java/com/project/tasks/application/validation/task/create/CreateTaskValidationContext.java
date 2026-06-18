package com.project.tasks.application.validation.task.create;

import com.project.tasks.domain.model.Task;
import com.project.tasks.domain.model.User;

public record CreateTaskValidationContext(
        Task task,
        User currentUser
) {
}
