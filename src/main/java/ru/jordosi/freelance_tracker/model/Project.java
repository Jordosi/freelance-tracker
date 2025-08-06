package ru.jordosi.freelance_tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

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
    @ManyToMany
    @JoinTable(
            name = "projects_freelancers",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "freelancer_id")
    )
    private Set<User> freelancers;
    @ManyToOne
    private User client;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
