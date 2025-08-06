package ru.jordosi.freelance_tracker.dto.reminder;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReminderUpdateDto {
    private String text;
    private LocalDateTime remindAt;
    private Long taskId;
}
