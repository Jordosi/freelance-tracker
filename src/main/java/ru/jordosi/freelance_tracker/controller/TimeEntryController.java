package ru.jordosi.freelance_tracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.TimeSummaryDto;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryCreateDto;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryDto;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.repository.TimeEntryRepository;
import ru.jordosi.freelance_tracker.service.ProjectService;
import ru.jordosi.freelance_tracker.service.TaskService;
import ru.jordosi.freelance_tracker.service.TimeEntryService;

@RestController
@RequestMapping("/api/time-entries")
@RequiredArgsConstructor
public class TimeEntryController {
    private final TimeEntryRepository timeEntryRepository;
    private final TimeEntryService timeEntryService;
    private final ProjectService projectService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TimeEntryDto> createTimeEntry(
            @RequestBody @Valid TimeEntryCreateDto dto,
            User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(timeEntryService.createTimeEntry(dto, user.getId()));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<Page<TimeEntryDto>> getTimeEntriesForTask(
            @PathVariable Long taskId,
            Pageable pageable,
            User user) {
        return ResponseEntity.ok(
                timeEntryService.getTimeEntriesForTask(taskId, user.getId(), pageable));
    }

    @GetMapping("/tasks/{taskId}/total-spent-time")
    public ResponseEntity<Integer> getTotalTimeSpentForTask(
            @PathVariable Long taskId,
            User user) {

        taskService.validateTaskAccess(taskId, user.getId());

        int totalMinutes = timeEntryRepository.getTotalTimeSpentByTask(taskId).orElse(0);

        return ResponseEntity.ok(totalMinutes);
    }

    @GetMapping("/projects/{projectId}/time-summary")
    public ResponseEntity<Page<TimeSummaryDto>> getTimeSummaryForProject(
            @PathVariable Long projectId,
            @PageableDefault(size = 30, sort = "entryDate", direction = Sort.Direction.DESC) Pageable pageable,
            User user) {

        projectService.validateProjectAccess(projectId, user.getId());

        Page<TimeSummaryDto> summary = timeEntryRepository.getTimeSpentByProject(projectId, pageable);

        return ResponseEntity.ok(summary);
    }
}
