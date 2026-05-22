package com.project.tasks.infrastructure.web.dto;

import com.project.tasks.domain.enumeration.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateTaskDTO(

        @NotBlank
        String title,

        String description,

        @NotNull
        TaskPriority priority,

        LocalDateTime dueDate
) {
}
