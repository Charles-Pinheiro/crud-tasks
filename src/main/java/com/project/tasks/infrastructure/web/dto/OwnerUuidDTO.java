package com.project.tasks.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Identificador do usuário responsável pela tarefa")
public record OwnerUuidDTO(

        @Schema(description = "UUID do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull
        UUID uuid
) {
}
