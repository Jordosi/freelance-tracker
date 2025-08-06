package ru.jordosi.freelance_tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.jordosi.freelance_tracker.model.Project;
import ru.jordosi.freelance_tracker.model.Task;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.repository.UserRepository;
import ru.jordosi.freelance_tracker.security.CurrentUserProvider;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccessControlService {
    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;

    public void validateProjectAccessForWrite(Project project) {
        User user = getCurrentUserOrThrow();
        if (user.getRole() != User.Role.ADMIN) {
            if (!project.getClient().getId().equals(user.getId())) {
                throw new AccessDeniedException("You are not allowed to access this project");
            }
        }
    }

    public void validateTaskAccessForWrite(Task task) {
        validateProjectAccessForWrite(task.getProject());
        User user = getCurrentUserOrThrow();
        if (user.getRole() != User.Role.ADMIN) {
            if (!task.getCreator().getId().equals(user.getId())) {
                throw new AccessDeniedException("You are not allowed to access this task");
            }
        }
    }

    public void validateProjectAccessForRead(Project project) {
        User user = getCurrentUserOrThrow();
        if (user.getRole() != User.Role.ADMIN) {
            if (!project.getClient().getId().equals(user.getId()) && !project.getFreelancers().contains(user)) {
                throw new AccessDeniedException("You are not allowed to access this project");
            }
        }
    }

    public void validateTaskAccessForRead(Task task) {
        validateProjectAccessForRead(task.getProject());
        User user = getCurrentUserOrThrow();
        if (user.getRole() != User.Role.ADMIN) {
            if (!task.getCreator().getId().equals(user.getId()) && !task.getProject().getFreelancers().contains(user)) {
                throw new AccessDeniedException("You are not allowed to access this task");
            }
        }
    }

    private User getCurrentUserOrThrow() {
        return userRepository.findById(currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
