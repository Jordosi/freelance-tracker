package ru.jordosi.freelance_tracker.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class TaskCreateDto {
    @NotBlank String title;
    String description;
    @NotBlank @Pattern(regexp = "HIGH|MEDIUM|LOW") String priority;
    @NotBlank @Pattern(regexp = "NEW|PENDING|COMPLETED|CANCELED") String status = "NEW";
    LocalDateTime deadline;
    Integer estimatedTime;
}
