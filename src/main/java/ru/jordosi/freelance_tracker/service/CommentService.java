package ru.jordosi.freelance_tracker.service;

import ru.jordosi.freelance_tracker.dto.comment.CommentCreateDto;
import ru.jordosi.freelance_tracker.model.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(CommentCreateDto dto, Long clientId);

    List<Comment> getCommentsByTask(Long taskId, Long userId);

    void deleteComment(Long commentId, Long clientId);
}
