package ru.jordosi.freelance_tracker.service;


import ru.jordosi.freelance_tracker.dto.task.TaskCreateDto;
import ru.jordosi.freelance_tracker.dto.task.TaskResponse;
import ru.jordosi.freelance_tracker.dto.task.TaskUpdateDto;
import ru.jordosi.freelance_tracker.model.Task;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskCreateDto dto, Long clientId);

    TaskResponse updateTask(Long taskId, TaskUpdateDto dto, Long clientId);

    TaskResponse updateTaskStatus(Long taskId, String newStatus, Long userId);

    List<TaskResponse> getTasksByProject(Long projectId);

    TaskResponse getTaskById(Long taskId);

    void cancelTask(Long taskId);

    Task getTaskByIdEntity(Long taskId);
}
