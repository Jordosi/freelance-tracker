package ru.jordosi.freelance_tracker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.jordosi.freelance_tracker.dto.project.ProjectCreateDto;
import ru.jordosi.freelance_tracker.dto.project.ProjectDto;
import ru.jordosi.freelance_tracker.model.User;

public interface ProjectService {
    ProjectDto createProject(ProjectCreateDto projectCreateDto, User currentUser);
    Page<ProjectDto> getProjectsByUser(User currentUser, Pageable pageable);
}
