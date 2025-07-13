package ru.jordosi.freelance_tracker.dto.time_entry;

import jakarta.validation.constraints.Min;
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
public class TimeEntryCreateDto {
    @NotNull Long taskId;
    @Min(1) Integer timeSpent;
    @NotNull
    LocalDateTime entryDate;
}
