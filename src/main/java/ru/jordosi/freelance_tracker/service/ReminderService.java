package ru.jordosi.freelance_tracker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.jordosi.freelance_tracker.dto.reminder.ReminderCreateDto;
import ru.jordosi.freelance_tracker.dto.reminder.ReminderDto;

public interface ReminderService {
    ReminderDto createReminder(ReminderCreateDto dto, Long userId);
    Page<ReminderDto> getRemindersForTask(Long taskId, Long userId, Pageable pageable);
}
