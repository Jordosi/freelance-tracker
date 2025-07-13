package ru.jordosi.freelance_tracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jordosi.freelance_tracker.model.Reminder;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    @EntityGraph(attributePaths = {"task"})
    List<Reminder> findByRemindAtBetweenAndIsSentFalse(
            LocalDateTime start,
            LocalDateTime end);

    Page<Reminder> findByTaskId(Long taskId, Pageable pageable);
}
