package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.domain.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    Optional<Comment> findByCommentIdx(long commentIdx);

    @Query("select c from Comment c join fetch c.user left join fetch c.parent where c.board.boardIdx = :boardIdx order by c.parent.commentIdx asc nulls first, c.commentIdx asc")
    List<Comment> findAllWithUserAndParentByBoardIdxOrderByParentIdxAscNullsFirstCommentIdxAsc(@Param("boardIdx") Long boardIdx);

    @Query("select count(c) from Comment c where c.board=:board")
    long findCommentNum(@Param("board") Board board);

}
