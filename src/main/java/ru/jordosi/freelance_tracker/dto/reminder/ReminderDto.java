package ru.jordosi.freelance_tracker.dto.reminder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jordosi.freelance_tracker.model.Task;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ReminderDto {
    private Long id;
    private Task task;
    private String text;
    private LocalDateTime remindAt;
    private boolean isSent;
}
