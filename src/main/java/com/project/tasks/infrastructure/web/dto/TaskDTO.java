package com.project.tasks.infrastructure.web.dto;

import com.project.tasks.domain.enumeration.TaskPriority;
import com.project.tasks.domain.enumeration.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Representação completa de uma tarefa")
public record TaskDTO(

        @Schema(description = "Identificador único da tarefa", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID uuid,

        @Schema(description = "Título da tarefa", example = "Implementar autenticação")
        String title,

        @Schema(description = "Descrição da tarefa", example = "Adicionar login e registro com JWT")
        String description,

        @Schema(description = "Status atual da tarefa", example = "TODO")
        TaskStatus status,

        @Schema(description = "Prioridade da tarefa", example = "HIGH")
        TaskPriority priority,

        @Schema(description = "Data de criação", example = "2026-06-30T10:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Data da última atualização", example = "2026-06-30T12:00:00")
        LocalDateTime updatedAt,

        @Schema(description = "Data limite para conclusão", example = "2026-07-15T18:00:00")
        LocalDateTime dueDate,

        @Schema(description = "Usuário responsável pela tarefa")
        OwnerDTO owner
) {
}
