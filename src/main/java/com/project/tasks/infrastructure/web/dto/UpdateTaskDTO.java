package com.project.tasks.infrastructure.web.dto;

import com.project.tasks.domain.enumeration.TaskPriority;
import com.project.tasks.domain.enumeration.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Dados para atualização parcial de uma tarefa")
public record UpdateTaskDTO(

        @Schema(description = "Novo título da tarefa", example = "Implementar autenticação JWT")
        String title,

        @Schema(description = "Nova descrição da tarefa", example = "Incluir refresh token")
        String description,

        @Schema(description = "Nova prioridade da tarefa", example = "MEDIUM")
        TaskPriority priority,

        @Schema(description = "Novo status da tarefa", example = "IN_PROGRESS")
        TaskStatus status,

        @Schema(description = "Nova data limite para conclusão", example = "2026-07-20T18:00:00")
        LocalDateTime dueDate
) {
}
