package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.comment.CommentCreateDto;
import ru.jordosi.freelance_tracker.exception.ResourceNotFoundException;
import ru.jordosi.freelance_tracker.model.Comment;
import ru.jordosi.freelance_tracker.model.Task;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.repository.CommentRepository;
import ru.jordosi.freelance_tracker.repository.UserRepository;
import ru.jordosi.freelance_tracker.security.CurrentUserProvider;
import ru.jordosi.freelance_tracker.service.AccessControlService;
import ru.jordosi.freelance_tracker.service.CommentService;
import ru.jordosi.freelance_tracker.service.TaskService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserRepository userRepository;
    private final AccessControlService accessControlService;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public Comment createComment(CommentCreateDto dto, Long clientId) {
        Task task = taskService.getTaskByIdEntity(dto.getTaskId());
        User user = userRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        accessControlService.validateTaskAccessForWrite(task);
        Comment comment = Comment.builder()
                .text(dto.getText())
                .author(user)
                .task(task)
                .build();
        return commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByTask(Long taskId, Long clientId) {
        Task task = taskService.getTaskByIdEntity(taskId);
        accessControlService.validateTaskAccessForRead(task);
        return commentRepository.findByTaskId(taskId);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long clientId) {
        Comment comment =  commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        accessControlService.validateTaskAccessForWrite(comment.getTask());
        commentRepository.delete(comment);
    }
}
