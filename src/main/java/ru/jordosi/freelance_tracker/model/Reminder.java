package ru.jordosi.freelance_tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="reminders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="task_id", nullable=false)
    private Task task;
    private String message;
    @Column(name="remind_at", nullable=false)
    private LocalDateTime remindAt;
    @Column(nullable=false, name="is_sent")
    private boolean isSent = false;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
