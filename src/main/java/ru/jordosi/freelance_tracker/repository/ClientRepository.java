package ru.jordosi.freelance_tracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.jordosi.freelance_tracker.model.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c FROM Client c WHERE " +
            "(:includeGlobal = true AND c.isGlobal = true) OR " +
            "(c.createdBy = :userId AND c.isGlobal = false AND c.isArchived = false)")
    Page<Client> findAccessibleClients(
            @Param("userId") Long userId,
            @Param("includeGlobal") boolean includeGlobal,
            Pageable pageable
    );

    @Query("SELECT c FROM Client c WHERE c.id = :id AND " +
            "(c.isGlobal = true OR c.createdBy = :userId)")
    Optional<Client> findByIdAndAccessible(
            @Param("id") Long id,
            @Param("userId") Long userId
    );
}
