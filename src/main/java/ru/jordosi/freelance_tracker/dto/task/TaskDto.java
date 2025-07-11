package ru.jordosi.freelance_tracker.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jordosi.freelance_tracker.model.Comment;
import ru.jordosi.freelance_tracker.model.Project;
import ru.jordosi.freelance_tracker.model.Task;
import ru.jordosi.freelance_tracker.model.TimeEntry;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Task.Priority priority;
    private Task.TaskStatus status;
    private LocalDateTime deadline;
    private Integer estimatedTime;
    private LocalDateTime createdAt;
    private Project project;
    private List<TimeEntry> timeEntries;
    private List<Comment> comments;
}
