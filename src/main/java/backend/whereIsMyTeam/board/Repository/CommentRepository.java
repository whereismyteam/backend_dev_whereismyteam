package backend.whereIsMyTeam.board.Repository;

import backend.whereIsMyTeam.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
