package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String cateName);
}
