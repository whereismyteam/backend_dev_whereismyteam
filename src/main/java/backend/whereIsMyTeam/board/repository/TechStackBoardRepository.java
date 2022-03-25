package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.domain.Comment;
import backend.whereIsMyTeam.board.domain.TechStackBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TechStackBoardRepository extends JpaRepository<TechStackBoard, Long> {

    @Query("select t.board from TechStackBoard t where t.techStack.stackIdx=:stack_idx")
    List<Board> findByTechStackIdx(@Param("stack_idx")long idx);

    //TechStackBoard findByStack(Long stackIdx);
}
