package ru.jordosi.freelance_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.jordosi.freelance_tracker.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByProjectId(Long projectId);

    @Query("SELECT t FROM Task t WHERE t.project.client.id = :clientId")
    List<Task> findAllByClientId(Long clientId);

    @Query("SELECT t FROM Task t JOIN t.project.freelancers f WHERE f.id = :freelancerId")
    List<Task> findAllByFreelancerId(Long freelancerId);
}
