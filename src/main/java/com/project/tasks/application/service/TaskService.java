package com.project.tasks.application.service;

import com.project.tasks.domain.enumeration.TaskStatus;
import com.project.tasks.domain.model.Task;
import com.project.tasks.domain.repository.TaskRepository;
import com.project.tasks.infrastructure.web.dto.UpdateTaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return repository.findById(id).orElseThrow();
//        .orElseThrow(() ->
//                new TaskNotFoundException(id));
    }

    @Transactional
    public Task create(Task task) {
        task.setStatus(TaskStatus.TODO);

        return repository.save(task);
    }

    @Transactional
    public void delete(Long id) {
        Task task = repository.findById(id)
                .orElseThrow();

        repository.delete(task);
    }

    @Transactional
    public Task update(Long id, UpdateTaskDTO dto) {
        Task task = repository.findById(id).orElseThrow();

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

        return repository.save(task);
    }
}
