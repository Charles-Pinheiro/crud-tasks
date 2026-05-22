package com.project.tasks.infrastructure.web.dto;

import com.project.tasks.domain.enumeration.TaskPriority;
import com.project.tasks.domain.enumeration.TaskStatus;

import java.time.LocalDateTime;


public record TaskDTO(

        Long id,

        String title,

        String description,

        TaskStatus status,

        TaskPriority priority,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        LocalDateTime dueDate
) {
}
