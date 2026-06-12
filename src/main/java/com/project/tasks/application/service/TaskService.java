package com.project.tasks.application.service;

import com.project.tasks.domain.enumeration.TaskStatus;
import com.project.tasks.domain.enumeration.UserRole;
import com.project.tasks.domain.model.Task;
import com.project.tasks.domain.model.User;
import com.project.tasks.domain.repository.TaskRepository;
import com.project.tasks.infrastructure.web.dto.UpdateTaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    // TODO - Transactional por que usar em leituras simples? (faz sentido a questão do readOnly?)

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
    public Task findById(Long id) {
        return findTaskForCurrentUser(id);
//        .orElseThrow(() ->
//                new TaskNotFoundException(id));
    }

    @Transactional
    public Task create(Task task) {
        User currentUser = getCurrentUser();

        task.setStatus(TaskStatus.TODO);
        task.setOwner(currentUser);

        return repository.save(task);
    }

    @Transactional
    public void delete(Long id) {
        Task task = findTaskForCurrentUser(id);

        repository.delete(task);
    }

    @Transactional
    public Task update(Long id, UpdateTaskDTO dto) {
        Task task = findTaskForCurrentUser(id);

        updateTaskFields(dto, task);

        return repository.save(task);
    }

    private void updateTaskFields(UpdateTaskDTO dto, Task task) {
        if (dto.title() != null) {
            task.setTitle(dto.title());
        }

        if (dto.description() != null) {
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

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Usuário não autenticado.");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Usuário autenticado inválido.");
        }

        return user;
    }

    private Task findTaskForCurrentUser(Long id) {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() == UserRole.ROLE_ADMIN) {
            return repository.findById(id).orElseThrow();
        }

        return repository.findByIdAndOwnerId(id, currentUser.getId()).orElseThrow();
    }
}
