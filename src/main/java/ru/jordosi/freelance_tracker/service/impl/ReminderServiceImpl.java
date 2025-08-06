package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.reminder.ReminderCreateDto;
import ru.jordosi.freelance_tracker.dto.reminder.ReminderUpdateDto;
import ru.jordosi.freelance_tracker.exception.ResourceNotFoundException;
import ru.jordosi.freelance_tracker.model.Reminder;
import ru.jordosi.freelance_tracker.model.Task;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.repository.ReminderRepository;
import ru.jordosi.freelance_tracker.repository.UserRepository;
import ru.jordosi.freelance_tracker.service.AccessControlService;
import ru.jordosi.freelance_tracker.service.NotificationService;
import ru.jordosi.freelance_tracker.service.ReminderService;
import ru.jordosi.freelance_tracker.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class ReminderServiceImpl implements ReminderService {
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final TaskService taskService;
    private final AccessControlService accessControlService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public Reminder createReminder(ReminderCreateDto dto, Long userId) {
        Task task = taskService.getTaskByIdEntity(dto.getTaskId());
        accessControlService.validateTaskAccessForRead(task);
        User user = getUserOrThrow(userId);

        Reminder reminder = Reminder.builder()
                .task(task)
                .creator(user)
                .message(dto.getMessage())
                .remindAt(dto.getRemindAt())
                .isSent(false)
                .build();

        return reminderRepository.save(reminder);
    }

    @Override
    @Transactional
    public Reminder updateReminder(Long id, ReminderUpdateDto dto, Long userId) {
        Reminder reminder = getReminderOrThrow(id);
        if (!reminder.getCreator().getId().equals(userId)) {
            throw new AccessDeniedException("You can only update your own reminders");
        }

        Task task = taskService.getTaskByIdEntity(dto.getTaskId());
        accessControlService.validateTaskAccessForRead(task);

        reminder.setTask(task);
        reminder.setMessage(dto.getText());
        reminder.setRemindAt(dto.getRemindAt());
        reminder.setSent(false);

        return reminderRepository.save(reminder);
    }

    @Override
    @Transactional
    public void deleteReminder(Long id, Long userId) {
        Reminder reminder = getReminderOrThrow(id);
        if (!reminder.getCreator().getId().equals(userId)) {
            throw new AccessDeniedException("You can only delete your own reminders");
        }
        reminderRepository.delete(reminder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reminder> getRemindersByTask(Long taskId, Long userId) {
        Task task = taskService.getTaskByIdEntity(taskId);
        accessControlService.validateTaskAccessForRead(task);
        return reminderRepository.findByTaskId(taskId);
    }

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void sendDueReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> reminders = reminderRepository
                .findByRemindAtBetweenAndIsSentFalse(now.minusMinutes(1), now);

        reminders.forEach(reminder -> {
            notificationService.sendNotification(
                    reminder.getCreator().getUsername(),
                    reminder.getMessage());
            reminder.setSent(true);
            reminderRepository.save(reminder);
        });
    }

    private Reminder getReminderOrThrow(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
