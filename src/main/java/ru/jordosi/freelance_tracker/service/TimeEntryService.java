package ru.jordosi.freelance_tracker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryCreateDto;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryDto;

public interface TimeEntryService {
    TimeEntryDto createTimeEntry(TimeEntryCreateDto timeEntryCreateDto, Long userId);
    Page<TimeEntryDto> getTimeEntriesForTask(Long taskId, Long userId, Pageable pageable);
}
