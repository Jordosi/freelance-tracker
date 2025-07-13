package ru.jordosi.freelance_tracker.dto.time_entry;

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
public class TimeEntryDto {
    private Long id;
    private Task task;
    private Integer timeSpent;
    private LocalDateTime entryDate;
    private LocalDateTime createdAt;
}
