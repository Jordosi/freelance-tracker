package ru.jordosi.freelance_tracker.service;

import ru.jordosi.freelance_tracker.dto.project.ProjectCreateDto;
import ru.jordosi.freelance_tracker.dto.project.ProjectUpdateDto;
import ru.jordosi.freelance_tracker.model.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(ProjectCreateDto dto, Long clientId);
    Project updateProject(Long projectId, ProjectUpdateDto dto, Long clientId);
    void assignFreelancer(Long projectId, Long freelancerId, Long clientId);
    void deleteFreelancer(Long projectId, Long freelancerId, Long clientId);
    List<Project> getProjectsForClient(Long clientId);
    List<Project> getProjectsForFreelancer(Long freelancerId);
    Project getProjectById(Long projectId, Long userId);
    List<Project> getAllProjects();
}
