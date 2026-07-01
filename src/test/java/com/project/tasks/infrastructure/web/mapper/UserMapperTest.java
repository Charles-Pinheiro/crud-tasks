package com.project.tasks.infrastructure.web.mapper;

import com.project.tasks.domain.model.User;
import com.project.tasks.infrastructure.web.dto.OwnerDTO;
import com.project.tasks.infrastructure.web.dto.OwnerUuidDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UserMapperTest {

    private final UserMapper mapper = new UserMapperImpl();

    @Nested
    class ToOwnerDTO {

        @Test
        void toOwnerDTO() {
            UUID uuid = UUID.randomUUID();
            User user = User.builder()
                    .name("name")
                    .build();
            user.setUuid(uuid);

            OwnerDTO dto = mapper.toOwnerDTO(user);

            Assertions.assertThat(dto)
                    .isNotNull()
                    .extracting(
                            OwnerDTO::uuid,
                            OwnerDTO::name
                    )
                    .containsExactly(
                            user.getUuid(),
                            user.getName()
                    );
        }

        @Test
        void toOwnerDTO_withNull() {
            User user = null;

            OwnerDTO dto = mapper.toOwnerDTO(user);

            Assertions.assertThat(dto).isNull();
        }
    }

    @Nested
    class ToEntity {

        @Test
        void toEntity() {
            OwnerUuidDTO dto = new OwnerUuidDTO(UUID.randomUUID());

            User entity = mapper.toEntity(dto);

            Assertions.assertThat(entity)
                    .isNotNull()
                    .extracting(User::getUuid)
                    .isEqualTo(dto.uuid());
        }

        @Test
        void toEntity_withNull() {
            OwnerUuidDTO dto = null;

            User entity = mapper.toEntity(dto);

            Assertions.assertThat(entity).isNull();
        }
    }
}
