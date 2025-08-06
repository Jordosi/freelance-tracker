package ru.jordosi.freelance_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.jordosi.freelance_tracker.model.Reminder;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByTaskId(Long taskId);

    @Query("SELECT r FROM Reminder r WHERE r.remindAt BETWEEN :from AND :to AND r.isSent = false")
    List<Reminder> findByRemindAtBetweenAndIsSentFalse(LocalDateTime from, LocalDateTime to);
}
