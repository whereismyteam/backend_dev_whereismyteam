package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.PostLike;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

public class PostLikeCustomRepositoryImpl implements PostLikeCustomRepository{

    //CustomRepository 의 구현체

    JPAQueryFactory jpaQueryFactory;

    public PostLikeCustomRepositoryImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

//    public Optional<PostLike> exist(Long userIdx, Long boardIdx){
//        PostLike pLike = jpaQueryFactory
//                .selectFrom(postLike)
//                .where(postLike.user.idx.eq(userIdx),
//                        postLike.board.idx.eq(boardIdx))
//                .fetchFirst();
//        return Optional.ofNullable(pLike);
//
//    }
//
//    public long findPostLikeNum(Long boardId) {
//        return jpaQueryFactory
//                .selectFrom(postLike)
//                .where(postLike.board.id.eq(boardId))
//                .fetchCount();
//    }
}
