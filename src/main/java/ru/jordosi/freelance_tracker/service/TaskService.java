package ru.jordosi.freelance_tracker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.jordosi.freelance_tracker.dto.CommentDto;
import ru.jordosi.freelance_tracker.dto.TimeEntryDto;
import ru.jordosi.freelance_tracker.dto.task.TaskCreateDto;
import ru.jordosi.freelance_tracker.dto.task.TaskDto;
import ru.jordosi.freelance_tracker.dto.task.TaskUpdateDto;

public interface TaskService {
    TaskDto createTask(TaskCreateDto dto, Long projectId, Long currentUserId);
    TaskDto updateTask(Long id, TaskUpdateDto dto, Long projectId, Long currentUserId);
    TaskDto findById(Long id, Long currentUserId);
    Page<TaskDto> findByProjectId(Long projectId, Long currentUserId, Pageable pageable);
    TaskDto closeTask(Long taskId, Long currentUserId);
    TaskDto reassignTask(Long taskId, Long projectId, Long currentUserId);

    TaskDto addTimeEntry(Long taskId, TimeEntryDto timeEntry, Long currentUserId);
    TaskDto addComment(Long taskId, CommentDto comment, Long currentUserId);
}
