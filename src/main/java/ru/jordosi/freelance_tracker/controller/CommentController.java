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
import ru.jordosi.freelance_tracker.service.CommentService;
import ru.jordosi.freelance_tracker.service.TaskService;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(
            @RequestBody @Valid CommentCreateDto dto,
            User user) {
        taskService.validateTaskAccess(dto.getTaskId(), user.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(dto, user.getId()));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<Page<CommentDto>> getCommentsForTask(
            @PathVariable Long taskId,
            Pageable pageable,
            User user) {
        taskService.validateTaskAccess(taskId, user.getId());
        return ResponseEntity.ok(
                commentService.getCommentsForTask(taskId, user.getId(), pageable));
    }
}
