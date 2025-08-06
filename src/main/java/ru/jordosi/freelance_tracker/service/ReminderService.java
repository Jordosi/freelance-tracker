package ru.jordosi.freelance_tracker.service;

import ru.jordosi.freelance_tracker.dto.reminder.ReminderCreateDto;
import ru.jordosi.freelance_tracker.dto.reminder.ReminderUpdateDto;
import ru.jordosi.freelance_tracker.model.Reminder;

import java.util.List;

public interface ReminderService {
    Reminder createReminder(ReminderCreateDto dto, Long freelancerId);

    List<Reminder> getRemindersByTask(Long taskId, Long freelancerId);

    Reminder updateReminder(Long reminderId, ReminderUpdateDto dto, Long freelancerId);

    void deleteReminder(Long reminderId, Long freelancerId);
}
