package ru.jordosi.freelance_tracker.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jordosi.freelance_tracker.dto.references.CommentRef;
import ru.jordosi.freelance_tracker.dto.references.ProjectRef;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<CommentRef> comments;

    public static TaskResponse of(TaskDto task) {
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
                .comments(task.getComments().stream().map(CommentRef::new).collect(Collectors.toList()))
                .build();
    }
}
