package com.project.tasks.infrastructure.web.mapper;

import com.project.tasks.domain.model.Task;
import com.project.tasks.infrastructure.web.dto.CreateTaskDTO;
import com.project.tasks.infrastructure.web.dto.TaskDTO;
import com.project.tasks.infrastructure.web.dto.UpdateTaskDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskMapper {

    Task toEntity(CreateTaskDTO dto);

    Task toEntity(UpdateTaskDTO dto);

    TaskDTO toTaskDTO(Task entity);

    List<TaskDTO> toListTaskDTO(List<Task> entities);
}
