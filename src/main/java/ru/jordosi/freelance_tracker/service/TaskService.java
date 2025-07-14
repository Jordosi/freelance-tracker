package ru.jordosi.freelance_tracker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.jordosi.freelance_tracker.dto.comment.CommentDto;
import ru.jordosi.freelance_tracker.dto.task.TaskResponse;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryDto;
import ru.jordosi.freelance_tracker.dto.task.TaskCreateDto;
import ru.jordosi.freelance_tracker.dto.task.TaskUpdateDto;
import ru.jordosi.freelance_tracker.model.Task;

public interface TaskService {
    TaskResponse createTask(TaskCreateDto dto, Long projectId, Long currentUserId);
    TaskResponse updateTask(Long id, TaskUpdateDto dto, Long projectId, Long currentUserId);
    TaskResponse findById(Long id, Long currentUserId);
    Page<TaskResponse> findByProjectId(Long projectId, Long currentUserId, Pageable pageable);
    TaskResponse closeTask(Long taskId, Long currentUserId);
    TaskResponse reassignTask(Long taskId, Long projectId, Long currentUserId);

    TaskResponse addTimeEntry(Long taskId, TimeEntryDto timeEntry, Long currentUserId);
    TaskResponse addComment(Long taskId, CommentDto comment, Long currentUserId);
    Task getTaskEntity(Long taskId, Long currentUserId);
    void validateTaskAccess(Long taskId, Long currentUserId);
}
