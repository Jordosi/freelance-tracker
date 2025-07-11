package ru.jordosi.freelance_tracker.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.jordosi.freelance_tracker.model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    @Query("SELECT t FROM Task t WHERE " +
            "t.project.id = :projectId AND " +
            "(COALESCE(:statuses) IS NULL OR t.status IN :statuses) AND " +
            "(COALESCE(:priorities) IS NULL OR t.priority IN :priorities) AND " +
            "(:deadlineFrom IS NULL OR t.deadline >= :deadlineFrom) AND " +
            "(:deadlineTo IS NULL OR t.deadline <= :deadlineTo) AND " +
            "(LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Task> findByFilters(
            @Param("projectId") Long projectId,
            @Param("statuses") Set<Task.TaskStatus> statuses,
            @Param("priorities") Set<Task.Priority> priorities,
            @Param("deadlineFrom") LocalDateTime deadlineFrom,
            @Param("deadlineTo") LocalDateTime deadlineTo,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    @Query("SELECT t FROM Task t WHERE " +
            "t.project.freelancer.id = :userId AND " +
            "((t.deadline BETWEEN :start AND :end) OR " +
            "(t.createdAt BETWEEN :start AND :end))")
    List<Task> findTasksInTimeRange(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT t FROM Task t WHERE " +
            "t.deadline < CURRENT_TIMESTAMP AND " +
            "t.status NOT IN ('COMPLETED', 'CANCELLED') AND " +
            "t.project.freelancer.id = :userId")
    List<Task> findOverdueTasks(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.project.id = :project_id")
    Page<Task> findTasksByProjectId(@Param("project_id") Long projectId, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "project.freelancer"})
    Optional<Task> findWithProjectById(Long id);
}
