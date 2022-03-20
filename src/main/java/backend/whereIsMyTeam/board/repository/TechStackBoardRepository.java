package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.domain.Comment;
import backend.whereIsMyTeam.board.domain.TechStackBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TechStackBoardRepository extends JpaRepository<TechStackBoard, Long> {

    Optional<TechStackBoard> findByStackIdx(long stackIdx);
}
