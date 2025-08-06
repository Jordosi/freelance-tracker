package ru.jordosi.freelance_tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.task.TaskCreateDto;
import ru.jordosi.freelance_tracker.dto.task.TaskResponse;
import ru.jordosi.freelance_tracker.dto.task.TaskUpdateDto;
import ru.jordosi.freelance_tracker.security.CurrentUserProvider;
import ru.jordosi.freelance_tracker.service.TaskService;
import ru.jordosi.freelance_tracker.service.TimeEntryService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final CurrentUserProvider currentUserProvider;
    private final TimeEntryService timeEntryService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(dto, currentUserProvider.getCurrentUserId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskUpdateDto dto) {
        return ResponseEntity.ok(taskService.updateTask(id, dto, currentUserProvider.getCurrentUserId()));
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status, currentUserProvider.getCurrentUserId()));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelTask(@PathVariable Long id) {
        taskService.cancelTask(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTaskByProject(@RequestParam Long  projectId) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping("/{id}/time-spent")
    public ResponseEntity<Integer> getTimeSpent(@PathVariable Long id) {
        return ResponseEntity.ok(timeEntryService.getTotalTimeForTask(id, currentUserProvider.getCurrentUserId()));
    }
}
