package ru.jordosi.freelance_tracker.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jordosi.freelance_tracker.dto.references.ProjectRef;
import ru.jordosi.freelance_tracker.model.Task;
import ru.jordosi.freelance_tracker.repository.ProjectRepository;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private LocalDateTime deadline;
    private Integer estimatedTime;
    private LocalDateTime createdAt;
    private ProjectRef project;
    private ProjectRepository  projectRepository;

    public static TaskResponse of(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .estimatedTime(task.getEstimatedTime())
                .status(task.getStatus().name())
                .priority(task.getPriority().name())
                .deadline(task.getDeadline())
                .estimatedTime(task.getEstimatedTime())
                .createdAt(task.getCreatedAt())
                .project(new ProjectRef(task.getProject()))
                .build();
    }
}
