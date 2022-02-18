package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>, PostLikeCustomRepository {
}
