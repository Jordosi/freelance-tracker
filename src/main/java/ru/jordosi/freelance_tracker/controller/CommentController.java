package ru.jordosi.freelance_tracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.comment.CommentCreateDto;
import ru.jordosi.freelance_tracker.model.Comment;
import ru.jordosi.freelance_tracker.security.CurrentUserProvider;
import ru.jordosi.freelance_tracker.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<Comment> createComment(@Valid @RequestBody CommentCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(dto, currentUserProvider.getCurrentUserId()));
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAllCommentsByTask(@RequestParam Long id) {
        return ResponseEntity.ok(commentService.getCommentsByTask(id, currentUserProvider.getCurrentUserId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id,  currentUserProvider.getCurrentUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
