package ru.jordosi.freelance_tracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.project.ProjectCreateDto;
import ru.jordosi.freelance_tracker.dto.project.ProjectDto;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.service.ProjectService;

import java.security.Principal;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @Valid @RequestBody ProjectCreateDto dto,
            Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(projectService.createProject(dto, user));
    }

    @GetMapping
    public ResponseEntity<Page<ProjectDto>> getProjectsByUser(
            Pageable pageable,
            Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(projectService.getProjectsByUser(user, pageable));
    }
}
