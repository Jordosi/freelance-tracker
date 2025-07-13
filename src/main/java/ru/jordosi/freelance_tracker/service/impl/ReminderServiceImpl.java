package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.reminder.ReminderCreateDto;
import ru.jordosi.freelance_tracker.dto.reminder.ReminderDto;
import ru.jordosi.freelance_tracker.model.Reminder;
import ru.jordosi.freelance_tracker.model.Task;
import ru.jordosi.freelance_tracker.repository.ReminderRepository;
import ru.jordosi.freelance_tracker.service.NotificationService;
import ru.jordosi.freelance_tracker.service.ReminderService;
import ru.jordosi.freelance_tracker.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {
    private final ReminderRepository reminderRepository;
    private final TaskService taskService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public ReminderDto createReminder(ReminderCreateDto dto, Long userId) {
        Task task = taskService.getTaskEntity(dto.getTask_id(), userId);

        Reminder reminder = Reminder.builder()
                .task(task)
                .message(dto.getMessage())
                .remindAt(dto.getRemindAt())
                .build();

        reminder = reminderRepository.save(reminder);
        return toDto(reminder);
    }

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void sendDueReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> reminders = reminderRepository
                .findByRemindAtBetweenAndIsSentFalse(now.minusMinutes(1), now.plusMinutes(1));

        reminders.forEach(reminder -> {
            notificationService.sendNotification(
                    reminder.getTask().getProject().getFreelancer().getEmail(),
                    reminder.getMessage());
            reminder.setSent(true);
            reminderRepository.save(reminder);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReminderDto> getRemindersForTask(Long taskId, Long userId, Pageable pageable) {
        taskService.validateTaskAccess(taskId, userId);

        return reminderRepository.findByTaskId(taskId, pageable)
                .map(this::toDto);
    }

    private ReminderDto toDto(Reminder reminder) {
        return ReminderDto.builder()
                .id(reminder.getId())
                .text(reminder.getMessage())
                .isSent(reminder.isSent())
                .task(reminder.getTask())
                .remindAt(reminder.getRemindAt())
                .build();
    }
}
