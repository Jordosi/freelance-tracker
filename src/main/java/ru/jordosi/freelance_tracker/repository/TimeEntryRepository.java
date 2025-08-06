package ru.jordosi.freelance_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.jordosi.freelance_tracker.model.TimeEntry;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Integer> {
    List<TimeEntry> findByTaskId(Long taskId);

    Optional<TimeEntry> findById(Long id);

    @Query("SELECT SUM(t.timeSpent) FROM TimeEntry t WHERE t.task.id = :taskId")
    Optional<Integer> getTotalTimeSpentByTask(Long taskId);

    @Query("SELECT SUM(t.timeSpent) FROM TimeEntry t WHERE t.task.project.id = :projectId")
    Optional<Integer> getTotalTimeSpentByProject(Long projectId);

    void deleteById(Long id);
}
