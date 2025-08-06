package ru.jordosi.freelance_tracker.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jordosi.freelance_tracker.dto.task.TaskResponse;
import ru.jordosi.freelance_tracker.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
    private Long id;
    private TaskResponse task;
    private String text;
    private User author;
    private LocalDateTime createdAt;
}
