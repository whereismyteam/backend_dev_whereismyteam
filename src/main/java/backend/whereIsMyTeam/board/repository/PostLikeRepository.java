package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.PostLike;
import backend.whereIsMyTeam.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>, PostLikeCustomRepository {

    Optional<PostLike> deleteBylikeIdx(long likeIdx);
}
