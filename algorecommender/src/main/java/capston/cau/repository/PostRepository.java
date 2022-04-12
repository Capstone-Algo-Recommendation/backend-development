package capston.cau.repository;

import capston.cau.domain.board.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface PostRepository extends JpaRepository<Post,Long> {

}
