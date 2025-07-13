package ru.jordosi.freelance_tracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.jordosi.freelance_tracker.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @EntityGraph(attributePaths = {"author"})
    Page<Comment> findByTaskIdOrderByCreatedAtDesc(Long taskId, Pageable pageable);

    @Modifying
    @Query("UPDATE Comment c SET c.text = :text WHERE c.id = :id AND c.author.id = :userId")
    int updateCommentText(@Param("id") Long id,
                          @Param("text") String text,
                          @Param("userId") Long userId);
}
