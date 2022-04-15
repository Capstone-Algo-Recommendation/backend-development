package capston.cau.repository;

import capston.cau.domain.board.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.comments WHERE p.id=:postId")
    Optional<Post> findByPostIdWithComment(@Param(value ="postId") Long postId);

    Page<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);
}
