package ru.jordosi.freelance_tracker.service;

import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryCreateDto;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryUpdateDto;
import ru.jordosi.freelance_tracker.model.TimeEntry;

import java.util.List;

public interface TimeEntryService {
    TimeEntry createTimeEntry(TimeEntryCreateDto dto, Long freelancerId);

    List<TimeEntry> getTimeEntriesByTask(Long taskId, Long userId);

    TimeEntry updateTimeEntry(Long entryId, TimeEntryUpdateDto dto, Long freelancerId);

    void deleteTimeEntry(Long entryId, Long freelancerId);

    Integer getTotalTimeForTask(Long taskId, Long userId);
}
