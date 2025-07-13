package ru.jordosi.freelance_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TimeSummaryDto {
    private LocalDateTime entryDate;
    private Long timeSpent;
}
