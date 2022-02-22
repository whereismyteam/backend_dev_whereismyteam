package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository <Board, Long> {

    Optional<Board> findByBoardIdx(long boardIdx);

    @Query("select b from Board b join fetch b.user where b.boardIdx = :boardIdx")
    Optional<Board> findByBoardIdxWithUser(Long boardIdx);
}
