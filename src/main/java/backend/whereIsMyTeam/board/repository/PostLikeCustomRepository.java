package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.PostLike;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

/**
 * PostLikeRepository에 상속시킬 interface
 * PostLikeRepository에서 'PostLikeCustomRepositoryImpl'에 작성된 로직을 실행할 수 있게 함
 **/
public interface PostLikeCustomRepository {

    Optional<PostLike> exist(Long userIdx, Long boardIdx);
    long findPostLikeNum(@Param("board_idx") Long boardIdx);
}
