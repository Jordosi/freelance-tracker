package ru.jordosi.freelance_tracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryCreateDto;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryUpdateDto;
import ru.jordosi.freelance_tracker.model.TimeEntry;
import ru.jordosi.freelance_tracker.security.CurrentUserProvider;
import ru.jordosi.freelance_tracker.service.TimeEntryService;

import java.util.List;

@RestController
@RequestMapping("/api/time-entries")
@RequiredArgsConstructor
public class TimeEntryController {
   private final TimeEntryService timeEntryService;
   private final CurrentUserProvider currentUserProvider;

    @PostMapping
   public ResponseEntity<TimeEntry> createTimeEntry(@Valid @RequestBody TimeEntryCreateDto timeEntry){
       return ResponseEntity.status(HttpStatus.CREATED).body(timeEntryService.createTimeEntry(timeEntry, currentUserProvider.getCurrentUserId()));
   }

   @PutMapping("/{id}")
   public ResponseEntity<TimeEntry> updateTimeEntry(@PathVariable Long id, @Valid @RequestBody TimeEntryUpdateDto dto){
       return ResponseEntity.ok(timeEntryService.updateTimeEntry(id, dto, currentUserProvider.getCurrentUserId()));
   }

   @GetMapping
   public ResponseEntity<List<TimeEntry>> getTimeEntriesByTask(@RequestParam Long taskId){
       return ResponseEntity.ok(timeEntryService.getTimeEntriesByTask(taskId, currentUserProvider.getCurrentUserId()));
   }

   @DeleteMapping
    public ResponseEntity<Void> deleteTimeEntry(@RequestParam Long id){
       timeEntryService.deleteTimeEntry(id, currentUserProvider.getCurrentUserId());
       return ResponseEntity.noContent().build();
   }
}
