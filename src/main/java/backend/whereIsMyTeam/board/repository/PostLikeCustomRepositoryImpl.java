package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.PostLike;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static backend.whereIsMyTeam.board.domain.QPostLike.postLike;

//CustomRepository 의 구현체
public class PostLikeCustomRepositoryImpl implements PostLikeCustomRepository{


    JPAQueryFactory jpaQueryFactory;

    public PostLikeCustomRepositoryImpl(EntityManager em){

        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    /**
     * 좋아요를 클릭여부를 확인하는 메서드
     * 리턴 값: null ? empty() : of(value);
     **/
    public Optional<PostLike> exist(Long userIdx, Long boardIdx){
        PostLike pLike = jpaQueryFactory
                .selectFrom(postLike)
                .where(postLike.user.userIdx.eq(userIdx),
                        postLike.board.boardIdx.eq(boardIdx))
                .fetchFirst();
        return Optional.ofNullable(pLike);

    }

    /**
     * boardIdx로
     * '해당 게시물의 좋아요 총 갯수'
     * 리턴 값: null ? empty() : of(value);
     **/
    public long findPostLikeNum(Long boardIdx) {
        return jpaQueryFactory
                .selectFrom(postLike)
                .where(postLike.board.boardIdx.eq(boardIdx))
                .fetchCount();
    }
}
