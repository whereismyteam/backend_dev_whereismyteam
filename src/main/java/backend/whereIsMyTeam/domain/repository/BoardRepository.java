package backend.whereIsMyTeam.domain.repository;

import backend.whereIsMyTeam.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
