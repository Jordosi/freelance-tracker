package ru.jordosi.freelance_tracker.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.jordosi.freelance_tracker.model.Task;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    @Query("SELECT t FROM Task t WHERE t.project.id = :project_id")
    Page<Task> findTasksByProjectId(@Param("project_id") Long projectId, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "project.freelancer"})
    Optional<Task> findWithProjectById(Long id);
}
