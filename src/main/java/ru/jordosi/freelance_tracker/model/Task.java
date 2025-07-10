package ru.jordosi.freelance_tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer estimatedTime;

    @ManyToOne
    private Project project;
    @OneToMany(mappedBy="task")
    private List<TimeEntry> timeEntries;
    @OneToMany(mappedBy="task")
    private List<Comment> comments;

    public enum Priority {
        HIGH, MEDIUM, LOW
    }
    public enum TaskStatus {
        NEW, IN_PROGRESS, COMPLETED
    }
}
