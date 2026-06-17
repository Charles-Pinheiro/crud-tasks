package com.project.tasks.infrastructure.web.mapper;

import com.project.tasks.domain.model.User;
import com.project.tasks.infrastructure.web.dto.OwnerDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    OwnerDTO toOwnerDTO(User entity);
}
