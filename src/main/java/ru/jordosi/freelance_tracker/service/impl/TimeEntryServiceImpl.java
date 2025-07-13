package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryCreateDto;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryDto;
import ru.jordosi.freelance_tracker.model.Task;
import ru.jordosi.freelance_tracker.model.TimeEntry;
import ru.jordosi.freelance_tracker.repository.TimeEntryRepository;
import ru.jordosi.freelance_tracker.service.TaskService;
import ru.jordosi.freelance_tracker.service.TimeEntryService;

@Service
@RequiredArgsConstructor
public class TimeEntryServiceImpl implements TimeEntryService {
    private final TimeEntryRepository timeEntryRepository;
    private final TaskService taskService;

    @Override
    @Transactional
    public TimeEntryDto createTimeEntry(TimeEntryCreateDto dto, Long userId) {
        Task task = taskService.getTaskEntity(dto.getTaskId(), userId);

        TimeEntry entry = TimeEntry.builder()
                .task(task)
                .entryDate(dto.getEntryDate())
                .timeSpent(dto.getTimeSpent())
                .build();
        entry = timeEntryRepository.save(entry);
        TimeEntryDto entryDto = createTimeEntryDto(entry);
        taskService.addTimeEntry(task.getId(), entryDto, userId);
        return entryDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TimeEntryDto> getTimeEntriesForTask(Long taskId, Long userId, Pageable pageable) {
        taskService.validateTaskAccess(taskId, userId);
        return timeEntryRepository.findByTaskId(taskId, pageable)
                .map(this::createTimeEntryDto);
    }

    private TimeEntryDto createTimeEntryDto(TimeEntry timeEntry) {
        return TimeEntryDto.builder()
                .id(timeEntry.getId())
                .entryDate(timeEntry.getEntryDate())
                .createdAt(timeEntry.getCreatedAt())
                .timeSpent(timeEntry.getTimeSpent())
                .task(timeEntry.getTask())
                .build();
    }
}
