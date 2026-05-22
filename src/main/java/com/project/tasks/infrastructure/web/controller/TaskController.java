package com.project.tasks.infrastructure.web.controller;

import com.project.tasks.application.service.TaskService;
import com.project.tasks.domain.model.Task;
import com.project.tasks.infrastructure.web.dto.CreateTaskDTO;
import com.project.tasks.infrastructure.web.dto.TaskDTO;
import com.project.tasks.infrastructure.web.dto.UpdateTaskDTO;
import com.project.tasks.infrastructure.web.mapper.TaskMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;
    private final TaskMapper mapper;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> findAll() {
        List<TaskDTO> response = mapper.toListTaskDTO(service.findAll());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findById(
            @PathVariable Long id
    ) {
        TaskDTO response = mapper.toTaskDTO(service.findById(id));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> create(
            @RequestBody @Valid CreateTaskDTO request
    ) throws URISyntaxException {
        Task task = mapper.toEntity(request);

        TaskDTO response = mapper.toTaskDTO(service.create(task));

        return ResponseEntity.created(new URI("/tasks/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateTaskDTO request
    ) {
        TaskDTO response = mapper.toTaskDTO(service.update(id, request));

        return ResponseEntity.ok(response);
    }
}
