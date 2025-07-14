package ru.jordosi.freelance_tracker.dto.references;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jordosi.freelance_tracker.model.Project;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRef {
    private Long id;
    private String name;

    public ProjectRef(Project project) {
        this.id = project.getId();
        this.name = project.getName();
    }
}
