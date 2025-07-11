package ru.jordosi.freelance_tracker.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jordosi.freelance_tracker.model.Task;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class TaskFilter {
    private Set<Task.Priority> priorities;
    private Set<Task.TaskStatus> taskStatuses;
    LocalDateTime deadlineFrom;
    LocalDateTime deadlineTo;
    String searchQuery;
}
