package com.project.tasks.domain.repository;

import com.project.tasks.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByUuid(UUID uuid);

    List<Task> findAllByOwnerId(Long ownerId);

    Optional<Task> findByUuidAndOwnerId(UUID uuid, Long ownerId);
}
