package com.project.tasks.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados resumidos do usuário responsável pela tarefa")
public record OwnerDTO(

        @Schema(description = "UUID do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID uuid,

        @Schema(description = "Nome do usuário", example = "João Silva")
        String name
) {
}
