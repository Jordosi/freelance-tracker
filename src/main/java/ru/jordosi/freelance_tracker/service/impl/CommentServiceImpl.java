package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.comment.CommentCreateDto;
import ru.jordosi.freelance_tracker.dto.comment.CommentDto;
import ru.jordosi.freelance_tracker.model.Comment;
import ru.jordosi.freelance_tracker.model.Task;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.repository.CommentRepository;
import ru.jordosi.freelance_tracker.repository.UserRepository;
import ru.jordosi.freelance_tracker.service.CommentService;
import ru.jordosi.freelance_tracker.service.TaskService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentDto createComment(CommentCreateDto dto, Long userId) {
        Task task = taskService.getTaskEntity(dto.getTaskId(), userId);
        User author = userRepository.getReferenceById(userId);

        Comment comment = Comment.builder()
                .task(task)
                .author(author)
                .text(dto.getText())
                .build();

        comment = commentRepository.save(comment);
        return toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentDto> getCommentsForTask(Long taskId, Long userId, Pageable pageable) {
        taskService.validateTaskAccess(taskId, userId);
        return commentRepository.findByTaskIdOrderByCreatedAtDesc(taskId, pageable)
                .map(this::toDto);
    }

    private CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .task(comment.getTask())
                .author(comment.getAuthor())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
