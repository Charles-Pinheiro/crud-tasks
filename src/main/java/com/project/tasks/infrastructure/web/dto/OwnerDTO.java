package com.project.tasks.infrastructure.web.dto;

import java.util.UUID;

public record OwnerDTO(

        UUID uuid,

        String name
) {
}
