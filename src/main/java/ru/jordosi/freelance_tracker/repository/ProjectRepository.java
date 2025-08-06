package ru.jordosi.freelance_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.jordosi.freelance_tracker.model.Project;

import java.util.List;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByClientId(Long clientId);

    @Query("SELECT p FROM Project p JOIN p.freelancers f WHERE f.id = :freelancerId")
    List<Project> findByFreelancerId(Long freelancerId);
}
