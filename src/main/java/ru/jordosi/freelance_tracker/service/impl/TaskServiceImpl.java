package ru.jordosi.freelance_tracker.service.impl;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.CommentDto;
import ru.jordosi.freelance_tracker.dto.TimeEntryDto;
import ru.jordosi.freelance_tracker.dto.task.TaskCreateDto;
import ru.jordosi.freelance_tracker.dto.task.TaskDto;
import ru.jordosi.freelance_tracker.dto.task.TaskUpdateDto;
import ru.jordosi.freelance_tracker.exception.ResourceNotFoundException;
import ru.jordosi.freelance_tracker.model.*;
import ru.jordosi.freelance_tracker.repository.ProjectRepository;
import ru.jordosi.freelance_tracker.repository.TaskRepository;
import ru.jordosi.freelance_tracker.service.TaskService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public TaskDto createTask(TaskCreateDto dto, Long projectId, Long currentUserId) {
        Project project = projectRepository.findByIdAndFreelancerId(projectId,  currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found or not accessible"));
        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(Task.TaskStatus.NEW)
                .priority(Task.Priority.valueOf(dto.getPriority()))
                .project(project)
                .deadline(dto.getDeadline())
                .estimatedTime(dto.getEstimatedTime())
                .build();
        return toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDto updateTask(Long id,TaskUpdateDto dto, Long projectId, Long currentUserId) {
        Project project = projectRepository.findByIdAndFreelancerId(projectId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found or not accessible"));
        Task task = taskRepository.findById(id)
                        .orElseThrow(() ->  new ResourceNotFoundException("Task not found"));
        if (!task.getProject().getId().equals(project.getId())) {
            throw new AccessDeniedException("You are not allowed to update this task");
        }

        return updateTaskFields(task, dto);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDto findById(Long id, Long currentUserId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        if (!task.getProject().getFreelancer().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not allowed to view this task");
        }
        return toDto(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDto> findByProjectId(Long projectId, Long currentUserId, Pageable pageable) {
        Project project = projectRepository.findByIdAndFreelancerId(projectId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found or not accessible"));
        return taskRepository.findTasksByProjectId(projectId, pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional
    public TaskDto closeTask(Long id, Long currentUserId) {
        Task task = taskRepository.findWithProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        validateTaskOwnership(task, currentUserId);

        if (task.getStatus() == Task.TaskStatus.COMPLETED) {
            throw new IllegalStateException("This task is already completed");
        }

        task.setStatus(Task.TaskStatus.COMPLETED);
        return toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDto reassignTask(Long id, Long projectId, Long currentUserId) {
        Task task = taskRepository.findWithProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        validateTaskOwnership(task, currentUserId);

        Project project = projectRepository.findByIdAndFreelancerId(projectId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found or not accessible"));
        if (task.getProject().equals(project)) {
            throw new AccessDeniedException("Task is already assigned to this project");
        }
        task.setProject(project);
        return toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDto addTimeEntry(Long id, TimeEntryDto timeEntryDto, Long currentUserId) {
        Task task = taskRepository.findWithProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        validateTaskOwnership(task, currentUserId);
        if (task.getStatus() == Task.TaskStatus.COMPLETED) {
            throw new IllegalStateException("This task is already completed");
        }
        TimeEntry timeEntry = TimeEntry.builder()
                .id(timeEntryDto.getId())
                .task(task)
                .timeSpent(timeEntryDto.getTimeSpent())
                .entryDate(timeEntryDto.getEntryDate())
                .build();
        boolean containsEntry = task.getTimeEntries().stream()
                .anyMatch(entry -> entry.getEntryDate().isEqual(timeEntry.getEntryDate()));
        if (containsEntry) {
            throw new IllegalStateException("Time entry is already assigned to this task");
        }
        List<TimeEntry> timeEntries = task.getTimeEntries();
        timeEntries.add(timeEntry);
        task.setTimeEntries(timeEntries);
        return toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDto addComment(Long id, CommentDto dto, Long currentUserId) {
        Task task =  taskRepository.findWithProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        validateTaskOwnership(task, currentUserId);
        Comment comment = Comment.builder()
                .id(dto.getId())
                .text(dto.getText())
                .author(dto.getAuthor())
                .task(task)
                .createdAt(dto.getCreatedAt())
                .build();
        boolean containsComment = task.getComments().stream()
                .anyMatch(c -> c.getText().equalsIgnoreCase(comment.getText()));
        if (containsComment) {
            throw new IllegalStateException("This comment is already assigned to this task");
        }
        List<Comment> comments = task.getComments();
        comments.add(comment);
        task.setComments(comments);
        return toDto(taskRepository.save(task));
    }

    private TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .deadline(task.getDeadline())
                .estimatedTime(task.getEstimatedTime())
                .comments(task.getComments())
                .createdAt(task.getCreatedAt())
                .project(task.getProject())
                .timeEntries(task.getTimeEntries())
                .build();
    }

    private TaskDto updateTaskFields(Task task, TaskUpdateDto dto) {
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        try {
            task.setStatus(Task.TaskStatus.valueOf(dto.getStatus()));
            task.setPriority(Task.Priority.valueOf(dto.getPriority()));
        }
        catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid task status or priority");
        }

        task.setDeadline(dto.getDeadline());
        task.setEstimatedTime(dto.getEstimatedTime());

        return toDto(taskRepository.save(task));
    }

    private void validateTaskOwnership(Task task, Long currentUserId) {
        if (!task.getProject().getFreelancer().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not allowed to view this task");
        }
    }
}
