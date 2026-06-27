package com.project.tasks.infrastructure.web.controller;

import com.project.tasks.application.service.TaskService;
import com.project.tasks.domain.enumeration.TaskPriority;
import com.project.tasks.domain.enumeration.TaskStatus;
import com.project.tasks.domain.model.Task;
import com.project.tasks.infrastructure.web.dto.CreateTaskDTO;
import com.project.tasks.infrastructure.web.dto.TaskDTO;
import com.project.tasks.infrastructure.web.dto.UpdateTaskDTO;
import com.project.tasks.infrastructure.web.mapper.TaskMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService service;
    @Mock
    private TaskMapper mapper;

    @InjectMocks
    private TaskController controller;

    @Test
    void shouldFindAll() {
        Task task = Task.builder()
                .title("Task test")
                .description("Description test")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .build();

        List<Task> tasks = List.of(task);

        TaskDTO taskDto = new TaskDTO(
                task.getUuid(),
                "Task test",
                "Description test",
                TaskStatus.TODO,
                TaskPriority.MEDIUM,
                null,
                null,
                null,
                null
        );
        List<TaskDTO> listDto = List.of(taskDto);

        Mockito.when(service.findAll())
                .thenReturn(tasks);
        Mockito.when(mapper.toListTaskDTO(tasks))
                .thenReturn(listDto);

        HttpStatusCode statusCodeExpected = HttpStatusCode.valueOf(200);

        ResponseEntity<List<TaskDTO>> response = controller.findAll();

        Assertions.assertEquals(listDto, response.getBody());
        Assertions.assertEquals(statusCodeExpected, response.getStatusCode());

        Mockito.verify(service).findAll();
        Mockito.verify(mapper).toListTaskDTO(tasks);
    }

    @Test
    void shouldFindByUuid() {
        UUID uuid = UUID.randomUUID();

        Task task = Task.builder()
                .title("Task test")
                .description("Description test")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .build();
        task.setUuid(uuid);

        TaskDTO taskDto = new TaskDTO(
                uuid,
                "Task test",
                "Description test",
                TaskStatus.TODO,
                TaskPriority.MEDIUM,
                null,
                null,
                null,
                null
        );

        Mockito.when(service.findByUuid(uuid))
                .thenReturn(task);
        Mockito.when(mapper.toTaskDTO(task))
                .thenReturn(taskDto);

        HttpStatusCode statusCodeExpected = HttpStatusCode.valueOf(200);

        ResponseEntity<TaskDTO> response = controller.findByUuid(uuid);

        Assertions.assertEquals(taskDto, response.getBody());
        Assertions.assertEquals(statusCodeExpected, response.getStatusCode());

        Mockito.verify(service).findByUuid(uuid);
        Mockito.verify(mapper).toTaskDTO(task);
    }

    @Test
    void shouldCreate() {
        CreateTaskDTO createDto = new CreateTaskDTO(
                "Task test",
                "Description test",
                TaskPriority.MEDIUM,
                null,
                null
        );

        Task task = Task.builder()
                .title("Task test")
                .description("Description test")
                .priority(TaskPriority.MEDIUM)
                .build();

        TaskDTO responseDto = new TaskDTO(
                task.getUuid(),
                "Task test",
                "Description test",
                TaskStatus.TODO,
                TaskPriority.MEDIUM,
                null,
                null,
                null,
                null
        );

        Mockito.when(mapper.toEntity(createDto))
                .thenReturn(task);
        Mockito.when(service.create(task))
                .thenReturn(task);
        Mockito.when(mapper.toTaskDTO(task))
                .thenReturn(responseDto);

        HttpStatusCode statusCodeExpected = HttpStatusCode.valueOf(201);

        ResponseEntity<TaskDTO> response = controller.create(createDto);

        Assertions.assertEquals(responseDto, response.getBody());
        Assertions.assertEquals(statusCodeExpected, response.getStatusCode());

        Mockito.verify(mapper).toEntity(createDto);
        Mockito.verify(service).create(task);
        Mockito.verify(mapper).toTaskDTO(task);
    }

    @Test
    void shouldDelete() {
        UUID uuid = UUID.randomUUID();

        Mockito.doNothing()
                .when(service).delete(uuid);

        HttpStatusCode statusCodeExpected = HttpStatusCode.valueOf(204);

        ResponseEntity<Void> response = controller.delete(uuid);

        Assertions.assertEquals(statusCodeExpected, response.getStatusCode());

        Mockito.verify(service).delete(uuid);
    }

    @Test
    void shouldUpdate() {
        UUID uuid = UUID.randomUUID();

        UpdateTaskDTO updateDto = new UpdateTaskDTO(
                "Task test",
                "Description test",
                TaskPriority.LOW,
                TaskStatus.PAUSED,
                null
        );

        Task task = Task.builder()
                .title("Task test")
                .description("Description test")
                .status(TaskStatus.PAUSED)
                .priority(TaskPriority.LOW)
                .build();
        task.setUuid(uuid);

        TaskDTO taskDto = new TaskDTO(
                uuid,
                "Task test",
                "Description test",
                TaskStatus.PAUSED,
                TaskPriority.LOW,
                null,
                null,
                null,
                null
        );

        Mockito.when(service.update(uuid, updateDto))
                .thenReturn(task);
        Mockito.when(mapper.toTaskDTO(task))
                .thenReturn(taskDto);

        HttpStatusCode statusCodeExpected = HttpStatusCode.valueOf(200);

        ResponseEntity<TaskDTO> response = controller.update(uuid, updateDto);

        Assertions.assertEquals(taskDto, response.getBody());
        Assertions.assertEquals(statusCodeExpected, response.getStatusCode());

        Mockito.verify(service).update(uuid, updateDto);
        Mockito.verify(mapper).toTaskDTO(task);
    }
}
