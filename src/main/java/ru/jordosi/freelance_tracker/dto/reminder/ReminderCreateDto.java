package ru.jordosi.freelance_tracker.dto.reminder;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ReminderCreateDto {
    @NotNull private Long task_id;
    @NotNull private String message;
    @NotNull private LocalDateTime remindAt;
}
