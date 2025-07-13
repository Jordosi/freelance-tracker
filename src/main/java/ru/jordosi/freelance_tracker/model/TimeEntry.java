package ru.jordosi.freelance_tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="time_entries")
@Builder(toBuilder=true)
public class TimeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="task_id", nullable=false)
    private Task task;
    private Integer timeSpent;
    private LocalDateTime entryDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
