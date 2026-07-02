package com.project.tasks.infrastructure.web.controller;

import com.project.tasks.application.service.TaskService;
import com.project.tasks.domain.model.Task;
import com.project.tasks.infrastructure.web.dto.CreateTaskDTO;
import com.project.tasks.infrastructure.web.dto.TaskDTO;
import com.project.tasks.infrastructure.web.dto.UpdateTaskDTO;
import com.project.tasks.infrastructure.web.mapper.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "Operações de CRUD para tarefas")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService service;
    private final TaskMapper mapper;

    @GetMapping
    @Operation(summary = "Listar tarefas", description = "Retorna todas as tarefas cadastradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<List<TaskDTO>> findAll() {
        List<TaskDTO> response = mapper.toTaskDTOList(service.findAll());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Buscar tarefa por UUID", description = "Retorna uma tarefa específica pelo identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<TaskDTO> findByUuid(
            @Parameter(description = "UUID da tarefa") @PathVariable UUID uuid
    ) {
        TaskDTO response = mapper.toTaskDTO(service.findByUuid(uuid));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar tarefa", description = "Cria uma nova tarefa para um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<TaskDTO> create(
            @RequestBody @Valid CreateTaskDTO request
    ) {
        Task task = mapper.toEntity(request);

        TaskDTO response = mapper.toTaskDTO(service.create(task));

        return ResponseEntity.created(URI.create("/tasks/" + response.uuid())).body(response);
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Excluir tarefa", description = "Remove uma tarefa pelo identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID da tarefa") @PathVariable UUID uuid
    ) {
        service.delete(uuid);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}")
    @Operation(summary = "Atualizar tarefa", description = "Atualiza parcialmente os dados de uma tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<TaskDTO> update(
            @Parameter(description = "UUID da tarefa") @PathVariable UUID uuid,
            @RequestBody @Valid UpdateTaskDTO request
    ) {
        TaskDTO response = mapper.toTaskDTO(service.update(uuid, request));

        return ResponseEntity.ok(response);
    }
}
