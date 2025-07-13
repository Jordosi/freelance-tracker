package ru.jordosi.freelance_tracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.reminder.ReminderCreateDto;
import ru.jordosi.freelance_tracker.dto.reminder.ReminderDto;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.service.ReminderService;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;

    @PostMapping
    public ResponseEntity<ReminderDto> createReminder(
            @RequestBody @Valid ReminderCreateDto dto,
            User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reminderService.createReminder(dto, user.getId()));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<Page<ReminderDto>> getRemindersForTask(
            @PathVariable Long taskId,
            Pageable pageable,
            User user) {
        return ResponseEntity.ok(
                reminderService.getRemindersForTask(taskId, user.getId(), pageable));
    }
}
