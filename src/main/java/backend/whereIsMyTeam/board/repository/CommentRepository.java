package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Comment;
import backend.whereIsMyTeam.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByCommentIdx(long commentIdx);
}
