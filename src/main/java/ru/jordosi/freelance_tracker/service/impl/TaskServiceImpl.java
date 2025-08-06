package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.task.TaskCreateDto;
import ru.jordosi.freelance_tracker.dto.task.TaskResponse;
import ru.jordosi.freelance_tracker.dto.task.TaskUpdateDto;
import ru.jordosi.freelance_tracker.exception.ResourceNotFoundException;
import ru.jordosi.freelance_tracker.model.Project;
import ru.jordosi.freelance_tracker.model.Task;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.repository.ProjectRepository;
import ru.jordosi.freelance_tracker.repository.TaskRepository;
import ru.jordosi.freelance_tracker.repository.UserRepository;
import ru.jordosi.freelance_tracker.service.AccessControlService;
import ru.jordosi.freelance_tracker.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AccessControlService  accessControlService;

    @Override
    @Transactional
    public TaskResponse createTask(TaskCreateDto dto, Long clientId) {
        User user = getUserOrThrow(clientId);
        if (user.getRole() != User.Role.CLIENT) {
            throw new AccessDeniedException("User not allowed to create task");
        }
        Project project = getProjectOrThrow(dto.getProjectId());
        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(Task.Priority.valueOf(dto.getPriority()))
                .status(Task.TaskStatus.NEW)
                .deadline(dto.getDeadline())
                .estimatedTime(dto.getEstimatedTime())
                .project(project)
                .creator(user)
                .build();
        return TaskResponse.of(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long taskId, TaskUpdateDto dto, Long clientId) {
        Task task = getTaskOrThrow(taskId);
        accessControlService.validateTaskAccessForWrite(task);
        Project project = dto.getProjectId() != null
                ? getProjectOrThrow(dto.getProjectId())
                : task.getProject();
        task.setProject(project);
        task.setTitle(dto.getTitle() != null ? dto.getTitle() : task.getTitle());
        task.setDescription(dto.getDescription()  != null ? dto.getDescription() : task.getDescription());
        task.setPriority(dto.getPriority() != null ? Task.Priority.valueOf(dto.getPriority()) : task.getPriority());
        task.setDeadline(dto.getDeadline() != null ? dto.getDeadline() : task.getDeadline());
        task.setEstimatedTime(dto.getEstimatedTime() != null  ? dto.getEstimatedTime() : task.getEstimatedTime());
        return TaskResponse.of(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponse updateTaskStatus(Long taskId, String newStatus, Long userId) {
        Task task = getTaskOrThrow(taskId);
        User user = getUserOrThrow(userId);
        if (user.getRole() != User.Role.FREELANCER) {
            throw new IllegalStateException("Freelancer is expected to update task status");
        }
        if (!task.getProject().getFreelancers().contains(user)) {
            throw new AccessDeniedException("You are not assigned to this task");
        }
        task.setStatus(Task.TaskStatus.valueOf(newStatus));
        return TaskResponse.of(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void cancelTask(Long taskId) {
        Task task = getTaskOrThrow(taskId);
        accessControlService.validateTaskAccessForWrite(task);
        if (task.getStatus().equals(Task.TaskStatus.CANCELED)) {
            throw new IllegalStateException("Task is already cancelled");
        }
        task.setStatus(Task.TaskStatus.CANCELED);
        taskRepository.save(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByProject(Long projectId) {
        Project project = getProjectOrThrow(projectId);
        accessControlService.validateProjectAccessForRead(project);
        return taskRepository.findByProjectId(projectId).stream().map(TaskResponse::of).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long taskId) {
        Task task = getTaskOrThrow(taskId);
        accessControlService.validateTaskAccessForRead(task);
        return TaskResponse.of(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Task getTaskByIdEntity(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    private Task getTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    private Project getProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
