package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.project.ProjectCreateDto;
import ru.jordosi.freelance_tracker.dto.project.ProjectDto;
import ru.jordosi.freelance_tracker.exception.ResourceNotFoundException;
import ru.jordosi.freelance_tracker.model.Client;
import ru.jordosi.freelance_tracker.model.Project;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.repository.ClientRepository;
import ru.jordosi.freelance_tracker.repository.ProjectRepository;
import ru.jordosi.freelance_tracker.service.ProjectService;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public ProjectDto createProject(ProjectCreateDto dto, User currentUser) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        Project project = Project.builder()
                .name(dto.getName())
                .description((dto.getDescription()))
                .freelancer(currentUser)
                .client(client)
                .build();
        project = projectRepository.save(project);
        return toDto(project);
    }

    @Override
    @Transactional(readOnly=true)
    public Page<ProjectDto> getProjectsByUser(User currentUser, Pageable pageable) {
        return projectRepository.findByFreelancerId(currentUser.getId(), pageable)
                .map(this::toDto);
    }
    private ProjectDto toDto(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .freelancerId(project.getFreelancer().getId())
                .clientId(project.getClient().getId())
                .createdAt(project.getCreatedAt())
                .build();
    }
}
