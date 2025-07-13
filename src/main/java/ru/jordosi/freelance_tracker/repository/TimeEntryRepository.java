package ru.jordosi.freelance_tracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.jordosi.freelance_tracker.dto.TimeSummaryDto;
import ru.jordosi.freelance_tracker.model.TimeEntry;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Integer> {
    @EntityGraph(attributePaths = {"task"})
    Page<TimeEntry> findByTaskId(Long taskId, Pageable pageable);

    @Query("SELECT SUM(te.timeSpent) FROM TimeEntry te WHERE te.task.id = :task_id")
    Optional<Integer> getTotalTimeSpentByTask(@Param("task_id") Long task_id);

    @Query("SELECT NEW ru.jordosi.freelance_tracker.dto.TimeSummaryDto(" +
            "te.entryDate, SUM(te.timeSpent)) " +
            "FROM TimeEntry te " +
            "WHERE te.task.project.id = :project_id "+
            "GROUP BY te.entryDate")
    Page<TimeSummaryDto> getTimeSpentByProject(@Param("project_id") Long project_id, Pageable pageable);
}
