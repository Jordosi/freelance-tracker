package ru.jordosi.freelance_tracker.dto.task;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TaskUpdateDto {
    private String title;
    private String description;
    @Pattern(regexp = "NEW|PENDING|COMPLETED|CANCELED")
    private String status;
    @Pattern(regexp = "HIGH|MEDIUM|LOW")
    private String priority;
    private LocalDateTime deadline;
    private Integer estimatedTime;
}
