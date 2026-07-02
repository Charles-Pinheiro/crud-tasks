package com.project.tasks.infrastructure.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para autenticação")
public record LoginRequestDTO(

        @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Senha do usuário", example = "senha1234")
        @NotBlank
        String password
) {
}
