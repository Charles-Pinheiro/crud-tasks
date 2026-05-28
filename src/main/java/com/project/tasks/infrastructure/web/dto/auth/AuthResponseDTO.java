package com.project.tasks.infrastructure.web.dto.auth;

public record AuthResponseDTO(

        String token,

        long expiresIn,

        String role
) {
}
