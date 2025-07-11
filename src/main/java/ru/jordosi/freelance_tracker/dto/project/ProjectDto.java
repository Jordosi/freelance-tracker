package ru.jordosi.freelance_tracker.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    Long freelancerId;
    Long clientId;
    LocalDateTime createdAt;
}
