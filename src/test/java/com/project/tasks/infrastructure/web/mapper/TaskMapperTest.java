package com.project.tasks.infrastructure.web.mapper;

import com.project.tasks.domain.enumeration.TaskPriority;
import com.project.tasks.domain.enumeration.TaskStatus;
import com.project.tasks.domain.model.Task;
import com.project.tasks.domain.model.User;
import com.project.tasks.infrastructure.web.dto.CreateTaskDTO;
import com.project.tasks.infrastructure.web.dto.OwnerUuidDTO;
import com.project.tasks.infrastructure.web.dto.TaskDTO;
import com.project.tasks.infrastructure.web.dto.UpdateTaskDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.groups.Tuple.tuple;

public class TaskMapperTest {

    private final UserMapper userMapper = new UserMapperImpl();
    private final TaskMapper mapper = new TaskMapperImpl(userMapper);

    @Nested
    class ToEntity {

        @Test
        void createTaskDTO_toEntity() {
            OwnerUuidDTO ownerDto = new OwnerUuidDTO(UUID.randomUUID());
            CreateTaskDTO dto = new CreateTaskDTO(
                    "title",
                    "description",
                    TaskPriority.MEDIUM,
                    LocalDateTime.of(2026, 1, 1, 12, 30),
                    ownerDto
            );

            Task entity = mapper.toEntity(dto);

            Assertions.assertThat(entity)
                    .isNotNull()
                    .extracting(
                            Task::getTitle,
                            Task::getDescription,
                            Task::getPriority,
                            Task::getDueDate,
                            task -> task.getOwner().getUuid()
                    )
                    .containsExactly(
                            dto.title(),
                            dto.description(),
                            dto.priority(),
                            dto.dueDate(),
                            ownerDto.uuid()
                    );
        }

        @Test
        void createTaskDTO_toEntity_withNull() {
            CreateTaskDTO dto = null;

            Task entity = mapper.toEntity(dto);

            Assertions.assertThat(entity)
                    .isNull();;
        }

        @Test
        void updateTaskDTO_toEntity() {
            UpdateTaskDTO dto = new UpdateTaskDTO(
                    "title",
                    "description",
                    TaskPriority.MEDIUM,
                    TaskStatus.IN_PROGRESS,
                    LocalDateTime.of(2026, 1, 1, 12, 30)
            );

            Task entity = mapper.toEntity(dto);

            Assertions.assertThat(entity)
                    .isNotNull()
                    .extracting(
                            Task::getTitle,
                            Task::getDescription,
                            Task::getPriority,
                            Task::getStatus,
                            Task::getDueDate
                    )
                    .containsExactly(
                            dto.title(),
                            dto.description(),
                            dto.priority(),
                            dto.status(),
                            dto.dueDate()
                    );
        }

        @Test
        void updateTaskDTO_toEntity_withNull() {
            UpdateTaskDTO dto = null;

            Task entity = mapper.toEntity(dto);

            Assertions.assertThat(entity)
                    .isNull();;
        }
    }

    @Nested
    class ToTaskDTO {

        @Test
        void toTaskDTO() {
            User owner = User.builder()
                    .name("name")
                    .build();
            owner.setUuid(UUID.randomUUID());

            Task entity = Task.builder()
                    .title("title")
                    .description("description")
                    .status(TaskStatus.PAUSED)
                    .priority(TaskPriority.LOW)
                    .dueDate(LocalDateTime.of(2026, 10, 29, 10, 0))
                    .owner(owner)
                    .build();
            entity.setUuid(UUID.randomUUID());

            TaskDTO dto = mapper.toTaskDTO(entity);

            Assertions.assertThat(dto)
                    .isNotNull()
                    .extracting(
                            TaskDTO::uuid,
                            TaskDTO::title,
                            TaskDTO::description,
                            TaskDTO::status,
                            TaskDTO::priority,
                            TaskDTO::createdAt,
                            TaskDTO::updatedAt,
                            TaskDTO::dueDate,
                            taskDto -> taskDto.owner().uuid(),
                            taskDto -> taskDto.owner().name()
                    )
                    .containsExactly(
                            entity.getUuid(),
                            entity.getTitle(),
                            entity.getDescription(),
                            entity.getStatus(),
                            entity.getPriority(),
                            entity.getCreatedAt(),
                            entity.getUpdatedAt(),
                            entity.getDueDate(),
                            owner.getUuid(),
                            owner.getName()
                    );
        }

        @Test
        void toTaskDTO_withNull() {
            Task entity = null;

            TaskDTO dto = mapper.toTaskDTO(entity);

            Assertions.assertThat(dto)
                    .isNull();;
        }
    }

    @Nested
    class ToTaskDTOList {

        @Test
        void toTaskDTOList() {
            User owner = User.builder()
                    .name("name")
                    .build();
            owner.setUuid(UUID.randomUUID());

            Task entity = Task.builder()
                    .title("title")
                    .description("description")
                    .status(TaskStatus.PAUSED)
                    .priority(TaskPriority.LOW)
                    .dueDate(LocalDateTime.of(2026, 10, 29, 10, 0))
                    .owner(owner)
                    .build();
            entity.setUuid(UUID.randomUUID());

            List<TaskDTO> dto = mapper.toTaskDTOList(List.of(entity));

            Assertions.assertThat(dto)
                    .isNotNull()
                    .extracting(
                            TaskDTO::uuid,
                            TaskDTO::title,
                            TaskDTO::description,
                            TaskDTO::status,
                            TaskDTO::priority,
                            TaskDTO::createdAt,
                            TaskDTO::updatedAt,
                            TaskDTO::dueDate,
                            taskDto -> taskDto.owner().uuid(),
                            taskDto -> taskDto.owner().name()
                    )
                    .containsExactly(tuple(
                            entity.getUuid(),
                            entity.getTitle(),
                            entity.getDescription(),
                            entity.getStatus(),
                            entity.getPriority(),
                            entity.getCreatedAt(),
                            entity.getUpdatedAt(),
                            entity.getDueDate(),
                            owner.getUuid(),
                            owner.getName()
                    ));
        }

        @Test
        void toTaskDTOList_withEmptyList() {
            List<TaskDTO> list = mapper.toTaskDTOList(List.of());

            Assertions.assertThat(list)
                    .isEmpty();
        }
    }
}
