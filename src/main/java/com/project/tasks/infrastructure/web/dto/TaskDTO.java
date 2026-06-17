package com.project.tasks.infrastructure.web.dto;

import com.project.tasks.domain.enumeration.TaskPriority;
import com.project.tasks.domain.enumeration.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;


public record TaskDTO(

        UUID uuid,

        String title,

        String description,

        TaskStatus status,

        TaskPriority priority,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        LocalDateTime dueDate,

        OwnerDTO owner
) {
}
