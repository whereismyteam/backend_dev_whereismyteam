package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository <Board, Long> {

    Optional<Board> findByBoardIdx(long boardIdx);

}
