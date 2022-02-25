package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Comment;
import backend.whereIsMyTeam.board.dto.postCommentDto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByCommentIdx(long commentIdx);

    @Query("select c from Comment c join fetch c.user left join fetch c.parent where c.board.boardIdx = :postIdx order by c.parent.commentIdx asc nulls first, c.commentIdx asc")
    List<Comment> findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(@Param("postIdx") Long postId);
}
