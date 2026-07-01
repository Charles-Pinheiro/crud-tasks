package com.project.tasks.application.service;

import com.project.tasks.application.exception.BusinessException;
import com.project.tasks.application.exception.ErrorConstants;
import com.project.tasks.application.validation.task.create.CreateTaskValidationChain;
import com.project.tasks.application.validation.task.create.CreateTaskValidationContext;
import com.project.tasks.domain.enumeration.TaskPriority;
import com.project.tasks.domain.enumeration.TaskStatus;
import com.project.tasks.domain.enumeration.UserRole;
import com.project.tasks.domain.model.Task;
import com.project.tasks.domain.model.User;
import com.project.tasks.domain.repository.TaskRepository;
import com.project.tasks.domain.repository.UserRepository;
import com.project.tasks.infrastructure.web.dto.UpdateTaskDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CreateTaskValidationChain createTaskValidationChain;

    @InjectMocks
    private TaskService service;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class FindAll {

        @Test
        void shouldFindAll_whenCurrentUserIsAdmin() {
            User currentUser = mockCurrentUser(UserRole.ROLE_ADMIN, null);
            Task task = buildTask();
            List<Task> tasks = List.of(task);
            authenticate(currentUser);

            Mockito.when(repository.findAll())
                    .thenReturn(tasks);

            List<Task> response = service.findAll();

            Assertions.assertThat(response)
                    .isEqualTo(tasks);

            Mockito.verify(repository).findAll();
            Mockito.verify(repository, Mockito.never()).findAllByOwnerId(Mockito.any());
        }

        @Test
        void shouldFindAllByOwner_whenCurrentUserIsNotAdmin() {
            Long ownerId = 1L;
            User currentUser = mockCurrentUser(UserRole.ROLE_USER, ownerId);
            Task task = buildTask();
            List<Task> tasks = List.of(task);
            authenticate(currentUser);

            Mockito.when(repository.findAllByOwnerId(ownerId))
                    .thenReturn(tasks);

            List<Task> response = service.findAll();

            Assertions.assertThat(response)
                    .isEqualTo(tasks);

            Mockito.verify(repository).findAllByOwnerId(ownerId);
            Mockito.verify(repository, Mockito.never()).findAll();
        }

        @Test
        void shouldThrowWhenAuthenticationIsNull() {
            Assertions.assertThatThrownBy(() -> service.findAll())
                    .isInstanceOf(BusinessException.class)
                    .extracting("error", "status")
                    .containsExactly(ErrorConstants.UNAUTHENTICATED, HttpStatus.UNAUTHORIZED);
        }

        @Test
        void shouldThrowWhenAuthenticationIsNotAuthenticated() {
            Authentication authentication = Mockito.mock(Authentication.class);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            Mockito.when(authentication.isAuthenticated())
                    .thenReturn(false);

            Assertions.assertThatThrownBy(() -> service.findAll())
                    .isInstanceOf(BusinessException.class)
                    .extracting("error", "status")
                    .containsExactly(ErrorConstants.UNAUTHENTICATED, HttpStatus.UNAUTHORIZED);
        }

        @Test
        void shouldThrowWhenPrincipalIsNotUser() {
            Authentication authentication = Mockito.mock(Authentication.class);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            Mockito.when(authentication.isAuthenticated())
                    .thenReturn(true);
            Mockito.when(authentication.getPrincipal())
                    .thenReturn("invalid-user");

            Assertions.assertThatThrownBy(() -> service.findAll())
                    .isInstanceOf(BusinessException.class)
                    .extracting("error", "status")
                    .containsExactly(ErrorConstants.INVALID_USER, HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    class FindByUuid {

        @Test
        void shouldFindByUuid_whenCurrentUserIsAdmin() {
            UUID uuid = UUID.randomUUID();
            User currentUser = mockCurrentUser(UserRole.ROLE_ADMIN, null);
            Task task = buildTask();
            authenticate(currentUser);

            Mockito.when(repository.findByUuid(uuid))
                    .thenReturn(Optional.of(task));

            Task response = service.findByUuid(uuid);

            Assertions.assertThat(response)
                    .isEqualTo(task);

            Mockito.verify(repository).findByUuid(uuid);
            Mockito.verify(repository, Mockito.never()).findByUuidAndOwnerId(Mockito.any(), Mockito.any());
        }

        @Test
        void shouldFindByUuidAndOwner_whenCurrentUserIsNotAdmin() {
            UUID uuid = UUID.randomUUID();
            Long ownerId = 1L;
            User currentUser = mockCurrentUser(UserRole.ROLE_USER, ownerId);
            Task task = buildTask();
            authenticate(currentUser);

            Mockito.when(repository.findByUuidAndOwnerId(uuid, ownerId))
                    .thenReturn(Optional.of(task));

            Task response = service.findByUuid(uuid);

            Assertions.assertThat(response)
                    .isEqualTo(task);

            Mockito.verify(repository).findByUuidAndOwnerId(uuid, ownerId);
            Mockito.verify(repository, Mockito.never()).findByUuid(Mockito.any());
        }

        @Test
        void shouldThrowWhenAdminDoesNotFindTask() {
            UUID uuid = UUID.randomUUID();
            User currentUser = mockCurrentUser(UserRole.ROLE_ADMIN, null);
            authenticate(currentUser);

            Mockito.when(repository.findByUuid(uuid))
                    .thenReturn(Optional.empty());

            Assertions.assertThatThrownBy(() -> service.findByUuid(uuid))
                    .isInstanceOf(BusinessException.class)
                    .extracting("status")
                    .isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void shouldThrowWhenUserDoesNotFindTaskForOwner() {
            UUID uuid = UUID.randomUUID();
            Long ownerId = 1L;
            User currentUser = mockCurrentUser(UserRole.ROLE_USER, ownerId);
            authenticate(currentUser);

            Mockito.when(repository.findByUuidAndOwnerId(uuid, ownerId))
                    .thenReturn(Optional.empty());

            Assertions.assertThatThrownBy(() -> service.findByUuid(uuid))
                    .isInstanceOf(BusinessException.class)
                    .extracting("status")
                    .isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class Create {

        @Test
        void shouldCreateTaskForCurrentUser() {
            User currentUser = mockCurrentUser(UserRole.ROLE_USER, null);
            Task task = buildTask();
            authenticate(currentUser);

            Mockito.when(repository.save(task))
                    .thenReturn(task);

            Task response = service.create(task);

            Assertions.assertThat(response)
                    .isEqualTo(task);

            Assertions.assertThat(task)
                    .extracting(
                            Task::getStatus,
                            Task::getOwner
                    )
                    .containsExactly(
                            TaskStatus.TODO,
                            currentUser
                    );

            Mockito.verify(createTaskValidationChain).validate(new CreateTaskValidationContext(task, currentUser));
            Mockito.verify(repository).save(task);
            Mockito.verifyNoInteractions(userRepository);
        }

        @Test
        void shouldCreateTaskForSelectedOwner_whenCurrentUserIsAdmin() {
            UUID ownerUuid = UUID.randomUUID();
            User currentUser = mockCurrentUser(UserRole.ROLE_ADMIN, null);
            User selectedOwner = User.builder()
                    .name("owner")
                    .build();
            selectedOwner.setUuid(ownerUuid);
            Task task = buildTask();
            task.setOwner(selectedOwner);
            authenticate(currentUser);

            Mockito.when(userRepository.findByUuid(ownerUuid))
                    .thenReturn(Optional.of(selectedOwner));
            Mockito.when(repository.save(task))
                    .thenReturn(task);

            Task response = service.create(task);

            Assertions.assertThat(response)
                    .isEqualTo(task);

            Assertions.assertThat(task)
                    .extracting(
                            Task::getStatus,
                            Task::getOwner
                    )
                    .containsExactly(
                            TaskStatus.TODO,
                            selectedOwner
                    );

            Mockito.verify(createTaskValidationChain).validate(new CreateTaskValidationContext(task, selectedOwner));
            Mockito.verify(userRepository).findByUuid(ownerUuid);
            Mockito.verify(repository).save(task);
        }

        @Test
        void shouldCreateTaskForAdmin_whenOwnerIsNotProvided() {
            User currentUser = mockCurrentUser(UserRole.ROLE_ADMIN, null);
            Task task = buildTask();
            authenticate(currentUser);

            Mockito.when(repository.save(task))
                    .thenReturn(task);

            Task response = service.create(task);

            Assertions.assertThat(response)
                    .isEqualTo(task);
            Assertions.assertThat(task.getOwner())
                    .isEqualTo(currentUser);

            Mockito.verifyNoInteractions(userRepository);
        }

        @Test
        void shouldThrowWhenAdminSelectedOwnerDoesNotExist() {
            UUID ownerUuid = UUID.randomUUID();
            User currentUser = mockCurrentUser(UserRole.ROLE_ADMIN, null);
            User ownerReference = User.builder().build();
            ownerReference.setUuid(ownerUuid);
            Task task = buildTask();
            task.setOwner(ownerReference);
            authenticate(currentUser);

            Mockito.when(userRepository.findByUuid(ownerUuid))
                    .thenReturn(Optional.empty());

            Assertions.assertThatThrownBy(() -> service.create(task))
                    .isInstanceOf(BusinessException.class)
                    .extracting("status")
                    .isEqualTo(HttpStatus.NOT_FOUND);

            Mockito.verify(userRepository).findByUuid(ownerUuid);
            Mockito.verifyNoInteractions(createTaskValidationChain);
            Mockito.verify(repository, Mockito.never()).save(Mockito.any());
        }
    }

    @Nested
    class Delete {

        @Test
        void shouldDeleteTask() {
            UUID uuid = UUID.randomUUID();
            User currentUser = mockCurrentUser(UserRole.ROLE_ADMIN, null);
            Task task = buildTask();
            authenticate(currentUser);

            Mockito.when(repository.findByUuid(uuid))
                    .thenReturn(Optional.of(task));

            service.delete(uuid);

            Mockito.verify(repository).findByUuid(uuid);
            Mockito.verify(repository).delete(task);
        }

        @Test
        void shouldThrowWhenTaskDoesNotExist() {
            UUID uuid = UUID.randomUUID();
            Long ownerId = 1L;
            User currentUser = mockCurrentUser(UserRole.ROLE_USER, ownerId);
            authenticate(currentUser);

            Mockito.when(repository.findByUuidAndOwnerId(uuid, ownerId))
                    .thenReturn(Optional.empty());

            Assertions.assertThatThrownBy(() -> service.delete(uuid))
                    .isInstanceOf(BusinessException.class)
                    .extracting("status")
                    .isEqualTo(HttpStatus.NOT_FOUND);

            Mockito.verify(repository).findByUuidAndOwnerId(uuid, ownerId);
            Mockito.verify(repository, Mockito.never()).delete(Mockito.any());
        }
    }

    @Nested
    class Update {

        @Test
        void shouldUpdateAllFields() {
            UUID uuid = UUID.randomUUID();
            Long ownerId = 1L;
            LocalDateTime dueDate = LocalDateTime.of(2026, 1, 1, 12, 30);
            User currentUser = mockCurrentUser(UserRole.ROLE_USER, ownerId);
            Task task = buildTask();
            UpdateTaskDTO dto = new UpdateTaskDTO(
                    "updated title",
                    "updated description",
                    TaskPriority.HIGH,
                    TaskStatus.IN_PROGRESS,
                    dueDate
            );
            authenticate(currentUser);

            Mockito.when(repository.findByUuidAndOwnerId(uuid, ownerId))
                    .thenReturn(Optional.of(task));
            Mockito.when(repository.save(task))
                    .thenReturn(task);

            Task response = service.update(uuid, dto);

            Assertions.assertThat(response)
                    .isEqualTo(task);
            Assertions.assertThat(task)
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

            Mockito.verify(repository).save(task);
        }

        @Test
        void shouldIgnoreNullAndBlankFields() {
            UUID uuid = UUID.randomUUID();
            Long ownerId = 1L;
            User currentUser = mockCurrentUser(UserRole.ROLE_USER, ownerId);
            Task task = buildTask();
            UpdateTaskDTO dto = new UpdateTaskDTO(
                    " ",
                    "",
                    null,
                    null,
                    null
            );
            authenticate(currentUser);

            Mockito.when(repository.findByUuidAndOwnerId(uuid, ownerId))
                    .thenReturn(Optional.of(task));
            Mockito.when(repository.save(task))
                    .thenReturn(task);

            Task response = service.update(uuid, dto);

            Assertions.assertThat(response)
                    .isEqualTo(task);
            Assertions.assertThat(task)
                    .extracting(
                            Task::getTitle,
                            Task::getDescription,
                            Task::getPriority,
                            Task::getStatus,
                            Task::getDueDate
                    )
                    .containsExactly(
                            "title",
                            "description",
                            TaskPriority.MEDIUM,
                            TaskStatus.TODO,
                            null
                    );

            Mockito.verify(repository).save(task);
        }

        @Test
        void shouldThrowWhenTaskDoesNotExist() {
            UUID uuid = UUID.randomUUID();
            Long ownerId = 1L;
            User currentUser = mockCurrentUser(UserRole.ROLE_USER, ownerId);
            UpdateTaskDTO dto = new UpdateTaskDTO(
                    "updated title",
                    "updated description",
                    TaskPriority.HIGH,
                    TaskStatus.IN_PROGRESS,
                    LocalDateTime.of(2026, 1, 1, 12, 30)
            );
            authenticate(currentUser);

            Mockito.when(repository.findByUuidAndOwnerId(uuid, ownerId))
                    .thenReturn(Optional.empty());

            Assertions.assertThatThrownBy(() -> service.update(uuid, dto))
                    .isInstanceOf(BusinessException.class)
                    .extracting("status")
                    .isEqualTo(HttpStatus.NOT_FOUND);

            Mockito.verify(repository).findByUuidAndOwnerId(uuid, ownerId);
            Mockito.verify(repository, Mockito.never()).save(Mockito.any());
        }
    }

    private void authenticate(User user) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        Mockito.when(authentication.isAuthenticated())
                .thenReturn(true);
        Mockito.when(authentication.getPrincipal())
                .thenReturn(user);
    }

    private User mockCurrentUser(UserRole role, Long id) {
        User user = Mockito.mock(User.class);

        Mockito.when(user.getRole())
                .thenReturn(role);

        if (id != null) {
            Mockito.when(user.getId())
                    .thenReturn(id);
        }

        return user;
    }

    private Task buildTask() {
        return Task.builder()
                .title("title")
                .description("description")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .build();
    }
}
