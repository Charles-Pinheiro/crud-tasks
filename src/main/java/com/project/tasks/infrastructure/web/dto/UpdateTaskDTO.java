package com.project.tasks.infrastructure.web.dto;

import com.project.tasks.domain.enumeration.TaskPriority;
import com.project.tasks.domain.enumeration.TaskStatus;

import java.time.LocalDateTime;

public record UpdateTaskDTO(

        String title,

        String description,

        TaskPriority priority,

        TaskStatus status,

        LocalDateTime dueDate
) {
}
