package ru.jordosi.freelance_tracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.jordosi.freelance_tracker.model.Project;

import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByFreelancerId(Long freelancerId, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE p.id = :projectId AND p.freelancer.id = :userId")
    Optional<Project> findByIdAndFreelancerId(
            @Param("projectId") Long projectId,
            @Param("userId") Long userId
    );
}
