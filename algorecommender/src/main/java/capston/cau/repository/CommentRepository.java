package capston.cau.repository;

import capston.cau.domain.board.Comment;
import capston.cau.domain.board.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
