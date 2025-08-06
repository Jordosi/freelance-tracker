package ru.jordosi.freelance_tracker.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jordosi.freelance_tracker.model.User;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdateDto {
    private String name;
    private String description;
    private Set<User> freelancers;
}
