package ru.jordosi.freelance_tracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.project.ProjectCreateDto;
import ru.jordosi.freelance_tracker.dto.project.ProjectUpdateDto;
import ru.jordosi.freelance_tracker.model.Project;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.service.ProjectService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> createProject(
            @Valid @RequestBody ProjectCreateDto dto,
            Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(dto, user.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectUpdateDto dto, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(projectService.updateProject(id, dto, user.getId()));
    }

    @PostMapping("/{id}/add-freelancer")
    public ResponseEntity<Void> assignFreelancer(@PathVariable Long id, @RequestParam Long freelancerId, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        projectService.assignFreelancer(id, freelancerId, user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/delete-freelancer")
    public ResponseEntity<Void> deleteFreelancer(@PathVariable Long id, @RequestParam Long freelancerId, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        projectService.deleteFreelancer(id, freelancerId, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/client")
    public ResponseEntity<List<Project>> getProjectsForClient(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(projectService.getProjectsForClient(user.getId()));
    }

    @GetMapping("/freelancer")
    public ResponseEntity<List<Project>> getProjectsForFreelancer(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(projectService.getProjectsForFreelancer(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(projectService.getProjectById(id, user.getId()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        if (!user.getRole().equals(User.Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can get access to all the projects");
        }
        return ResponseEntity.ok(projectService.getAllProjects());
    }
}
