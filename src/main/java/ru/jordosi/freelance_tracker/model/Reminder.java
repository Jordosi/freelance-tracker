package ru.jordosi.freelance_tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ManyToOne
    private Task task;
    private String message;
    private LocalDateTime remindAt;
    private boolean isSent;
}
