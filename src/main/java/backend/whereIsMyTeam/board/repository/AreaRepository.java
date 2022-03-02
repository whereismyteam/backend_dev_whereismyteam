package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {

    Optional<Area> findByName(String name);

}
