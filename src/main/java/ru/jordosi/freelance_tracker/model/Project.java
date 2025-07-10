package ru.jordosi.freelance_tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="projects")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String description;
    @ManyToOne
    private User freelancer;
    @ManyToOne
    private Client client;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
