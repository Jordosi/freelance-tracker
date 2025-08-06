package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.project.ProjectCreateDto;
import ru.jordosi.freelance_tracker.dto.project.ProjectUpdateDto;
import ru.jordosi.freelance_tracker.exception.ResourceNotFoundException;
import ru.jordosi.freelance_tracker.model.Project;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.repository.ProjectRepository;
import ru.jordosi.freelance_tracker.repository.UserRepository;
import ru.jordosi.freelance_tracker.service.AccessControlService;
import ru.jordosi.freelance_tracker.service.ProjectService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AccessControlService accessControlService;

    @Override
    @Transactional
    public Project createProject(ProjectCreateDto projectCreateDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        if (user.getRole() != User.Role.CLIENT) {
            throw new AccessDeniedException("You are not allowed to perform this action");
        }
        Project project = Project.builder()
                .name(projectCreateDto.getName())
                .description(projectCreateDto.getDescription())
                .client(user)
                .build();
        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public Project updateProject(Long id, ProjectUpdateDto dto, Long userId) {
        Project project = getProjectOrThrow(id);
        accessControlService.validateProjectAccessForWrite(project);
        project.setName(dto.getName()!= null ? dto.getName() : project.getName());
        project.setDescription(dto.getDescription() != null ? dto.getDescription() : project.getDescription());

        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public void assignFreelancer(Long projectId, Long freelancerId, Long userId) {
        Project project = getProjectOrThrow(projectId);
        accessControlService.validateProjectAccessForWrite(project);
        User freelancer = userRepository.findById(freelancerId).orElseThrow(() -> new ResourceNotFoundException("Freelancer not found"));
        if (!freelancer.getRole().equals(User.Role.FREELANCER)) {
            throw new IllegalStateException("User to be excluded is not a freelancer");
        }
        Set<User> freelancers = project.getFreelancers();
        freelancers.add(freelancer);
        project.setFreelancers(freelancers);
        projectRepository.save(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getProjectsForClient(Long clientId) {
        return projectRepository.findByClientId(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getProjectsForFreelancer(Long freelancerId) {
        return projectRepository.findByFreelancerId(freelancerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Project getProjectById(Long projectId, Long userId) {
        Project project = getProjectOrThrow(projectId);
        accessControlService.validateProjectAccessForRead(project);
        return project;
    }

    @Override
    @Transactional
    public void deleteFreelancer(Long projectId, Long freelancerId, Long userId) {
        Project project = getProjectOrThrow(projectId);
        accessControlService.validateProjectAccessForWrite(project);
        Set<User> freelancers = project.getFreelancers();
        User freelancer =  userRepository.findById(freelancerId).orElseThrow(() -> new ResourceNotFoundException("Freelancer not found"));
        if (!freelancer.getRole().equals(User.Role.FREELANCER)) {
            throw new IllegalStateException("User to be excluded is not a freelancer");
        }
        freelancers.remove(freelancer);
        project.setFreelancers(freelancers);
        projectRepository.save(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    private Project getProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }
}
