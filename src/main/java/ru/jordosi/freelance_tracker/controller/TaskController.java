package ru.jordosi.freelance_tracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.CommentDto;
import ru.jordosi.freelance_tracker.dto.TimeEntryDto;
import ru.jordosi.freelance_tracker.dto.task.TaskCreateDto;
import ru.jordosi.freelance_tracker.dto.task.TaskDto;
import ru.jordosi.freelance_tracker.dto.task.TaskUpdateDto;
import ru.jordosi.freelance_tracker.security.CurrentUserProvider;
import ru.jordosi.freelance_tracker.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskCreateDto dto,
                                              @RequestParam Long projectId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(dto, projectId, currentUserProvider.getCurrentUserId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateDto dto,
                                              @RequestParam Long projectId) {
        return ResponseEntity.ok(taskService.updateTask(id, dto, projectId, currentUserProvider.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id, currentUserProvider.getCurrentUserId()));
    }

    @GetMapping
    public ResponseEntity<Page<TaskDto>> getByProjectId(@RequestParam Long projectId,
                                                    @PageableDefault(sort = "createdAt",
                                                            direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(taskService.findByProjectId(projectId, currentUserProvider.getCurrentUserId(), pageable));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<TaskDto> closeTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.closeTask(id, currentUserProvider.getCurrentUserId()));
    }

    @PostMapping("/{id}/reassign")
    public ResponseEntity<TaskDto> reassignTask(@PathVariable Long id, @RequestParam Long projectId) {
        return ResponseEntity.ok(taskService.reassignTask(id, projectId, currentUserProvider.getCurrentUserId()));
    }

    @PostMapping("/{id}/time-entries")
    public ResponseEntity<TaskDto> addTimeEntry(
            @PathVariable Long id,
            @RequestBody @Valid TimeEntryDto dto) {
        return ResponseEntity.ok(
                taskService.addTimeEntry(id, dto, currentUserProvider.getCurrentUserId()));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<TaskDto> addComment(
            @PathVariable Long id,
            @RequestBody @Valid CommentDto dto) {
        return ResponseEntity.ok(
                taskService.addComment(id, dto, currentUserProvider.getCurrentUserId()));
    }
}
