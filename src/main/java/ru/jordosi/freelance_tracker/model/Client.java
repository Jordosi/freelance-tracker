package ru.jordosi.freelance_tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    private String email;
    @Column(name="contact_info")
    private String contactInfo;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
