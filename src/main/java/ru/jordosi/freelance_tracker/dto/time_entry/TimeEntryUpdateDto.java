package ru.jordosi.freelance_tracker.dto.time_entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeEntryUpdateDto {
    private Integer timeSpent;
    private Long taskId;
    private LocalDateTime entryDate;
}
