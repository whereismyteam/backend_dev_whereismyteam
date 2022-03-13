package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Comment;
import backend.whereIsMyTeam.board.domain.TechStackBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechStackBoardRepository extends JpaRepository<TechStackBoard, Long> {

}
