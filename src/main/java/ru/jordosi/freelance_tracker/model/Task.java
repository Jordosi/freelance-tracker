package ru.jordosi.freelance_tracker.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.jordosi.freelance_tracker.dto.task.TaskResponse;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name="tasks")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder=true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDateTime deadline;
    @Column(name="estimated_time")
    private Integer estimatedTime;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public enum Priority {
        HIGH, MEDIUM, LOW
    }
    public enum TaskStatus {
        NEW, PENDING, COMPLETED, CANCELED
    }
}
