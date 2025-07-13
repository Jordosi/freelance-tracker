package ru.jordosi.freelance_tracker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.jordosi.freelance_tracker.dto.comment.CommentCreateDto;
import ru.jordosi.freelance_tracker.dto.comment.CommentDto;

public interface CommentService {
    public CommentDto createComment(CommentCreateDto dto, Long userId);
    public Page<CommentDto> getCommentsForTask(Long taskId, Long userId, Pageable pageable);

}
