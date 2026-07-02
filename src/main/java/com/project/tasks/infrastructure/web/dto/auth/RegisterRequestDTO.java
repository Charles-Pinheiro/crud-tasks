package com.project.tasks.infrastructure.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para registro de um novo usuário")
public record RegisterRequestDTO(

        @Schema(description = "Nome completo do usuário", example = "João Silva")
        @NotBlank
        String name,

        @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Senha com no mínimo 8 caracteres", example = "senha1234")
        @NotBlank
        @Size(min = 8)
        String password
) {
}
