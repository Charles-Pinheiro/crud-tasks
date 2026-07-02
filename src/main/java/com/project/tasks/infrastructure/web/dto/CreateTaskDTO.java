package com.project.tasks.infrastructure.web.dto;

import com.project.tasks.domain.enumeration.TaskPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Dados para criação de uma nova tarefa")
public record CreateTaskDTO(

        @Schema(description = "Título da tarefa", example = "Implementar autenticação")
        @NotBlank
        String title,

        @Schema(description = "Descrição detalhada da tarefa", example = "Adicionar login e registro com JWT")
        String description,

        @Schema(description = "Prioridade da tarefa", example = "HIGH")
        @NotNull
        TaskPriority priority,

        @Schema(description = "Data limite para conclusão", example = "2026-07-15T18:00:00")
        LocalDateTime dueDate,

        @Schema(description = "Identificador do usuário responsável pela tarefa")
        @Valid
        OwnerUuidDTO owner
) {
}
