package com.project.tasks.infrastructure.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação com token JWT")
public record AuthResponseDTO(

        @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Tempo de expiração do token em segundos", example = "3600")
        long expiresIn,

        @Schema(description = "Papel do usuário autenticado", example = "ROLE_USER")
        String role
) {
}
