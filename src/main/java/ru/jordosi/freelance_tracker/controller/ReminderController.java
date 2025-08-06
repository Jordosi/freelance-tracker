package ru.jordosi.freelance_tracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.reminder.ReminderCreateDto;
import ru.jordosi.freelance_tracker.dto.reminder.ReminderUpdateDto;
import ru.jordosi.freelance_tracker.model.Reminder;
import ru.jordosi.freelance_tracker.security.CurrentUserProvider;
import ru.jordosi.freelance_tracker.service.ReminderService;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<Reminder> createReminder(@Valid @RequestBody ReminderCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reminderService.createReminder(dto, currentUserProvider.getCurrentUserId()));
    }

    @GetMapping
    public ResponseEntity<List<Reminder>> getRemindersByTask(@RequestParam Long taskId) {
        return ResponseEntity.ok(reminderService.getRemindersByTask(taskId, currentUserProvider.getCurrentUserId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reminder>  updateReminder(@PathVariable Long id, @RequestBody ReminderUpdateDto dto) {
        return ResponseEntity.ok(reminderService.updateReminder(id, dto, currentUserProvider.getCurrentUserId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable Long id) {
        reminderService.deleteReminder(id, currentUserProvider.getCurrentUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
