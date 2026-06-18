package com.project.tasks.application.service;

import com.project.tasks.application.exception.BusinessException;
import com.project.tasks.application.exception.ErrorConstants;
import com.project.tasks.domain.enumeration.TaskStatus;
import com.project.tasks.domain.enumeration.UserRole;
import com.project.tasks.domain.model.Task;
import com.project.tasks.domain.model.User;
import com.project.tasks.domain.repository.TaskRepository;
import com.project.tasks.infrastructure.web.dto.UpdateTaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    // TODO - DesignPatterns - (chain of responsibility) -> Validação do create task por exemplo -> cadeia de validação com um objetivo

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() == UserRole.ROLE_ADMIN) {
            return repository.findAll();
        }

        return repository.findAllByOwnerId(currentUser.getId());
    }

    @Transactional(readOnly = true)
    public Task findByUuid(UUID uuid) {
        return findTaskForCurrentUser(uuid);
    }

    @Transactional
    public Task create(Task task) {
        User currentUser = getCurrentUser();

        task.setStatus(TaskStatus.TODO);
        task.setOwner(currentUser);

        return repository.save(task);
    }

    @Transactional
    public void delete(UUID uuid) {
        Task task = findTaskForCurrentUser(uuid);

        repository.delete(task);
    }

    @Transactional
    public Task update(UUID uuid, UpdateTaskDTO dto) {
        Task task = findTaskForCurrentUser(uuid);

        updateTaskFields(dto, task);

        return repository.save(task);
    }

    private void updateTaskFields(UpdateTaskDTO dto, Task task) {
        if (dto.title() != null && !dto.title().isBlank()) {
            task.setTitle(dto.title());
        }

        if (dto.description() != null && !dto.description().isBlank()) {
            task.setDescription(dto.description());
        }

        if (dto.priority() != null) {
            task.setPriority(dto.priority());
        }

        if (dto.status() != null) {
            task.setStatus(dto.status());
        }

        if (dto.dueDate() != null) {
            task.setDueDate(dto.dueDate());
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        BusinessException.throwIf(
                authentication == null || !authentication.isAuthenticated(),
                ErrorConstants.UNAUTHENTICATED,
                HttpStatus.UNAUTHORIZED
        );

        Object principal = authentication.getPrincipal();

        BusinessException.throwIf(
                !(principal instanceof User),
                ErrorConstants.INVALID_USER,
                HttpStatus.UNAUTHORIZED
        );

        return (User) principal;
    }

    private Task findTaskForCurrentUser(UUID uuid) {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() == UserRole.ROLE_ADMIN) {
            return repository.findByUuid(uuid)
                    .orElseThrow(BusinessException.notFound(Task.class));
        }

        return repository.findByUuidAndOwnerId(uuid, currentUser.getId())
                .orElseThrow(BusinessException.notFound(Task.class));
    }
}
