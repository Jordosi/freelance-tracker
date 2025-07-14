package ru.jordosi.freelance_tracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.comment.CommentCreateDto;
import ru.jordosi.freelance_tracker.dto.comment.CommentDto;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.security.CurrentUserProvider;
import ru.jordosi.freelance_tracker.service.CommentService;
import ru.jordosi.freelance_tracker.service.TaskService;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final TaskService taskService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(
            @RequestBody @Valid CommentCreateDto dto) {
        taskService.validateTaskAccess(dto.getTaskId(), currentUserProvider.getCurrentUserId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(dto, currentUserProvider.getCurrentUserId()));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<Page<CommentDto>> getCommentsForTask(
            @PathVariable Long taskId,
            Pageable pageable,
            User user) {
        taskService.validateTaskAccess(taskId, currentUserProvider.getCurrentUserId());
        return ResponseEntity.ok(
                commentService.getCommentsForTask(taskId, currentUserProvider.getCurrentUserId(), pageable));
    }
}
