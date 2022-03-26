package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.domain.BoardStatus;
import backend.whereIsMyTeam.board.dto.MainBoardListResponseDto;
import org.springframework.data.domain.Page;
import backend.whereIsMyTeam.user.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository <Board, Long> , BoardRepositoryCustom{

    Optional<Board> findByBoardIdx(long boardIdx);

    List<Board> findByWriter(User user);

    @EntityGraph(attributePaths = {"writer"})
    Optional<Board> findWithWriterByBoardIdx(Long userIdx);




    /**
     * 좋아요순(조회수)
     * [조건] : lastArticleIdx 필요 x
     **/
    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx " +
            "and " +
            " ((( b.cnt = (select bc.cnt from Board bc where bc.boardIdx= :lastIdx )) " +
            "and (b.boardIdx < :lastIdx)) " +
            "or ((( b.cnt < (select bc.cnt from Board bc where bc.boardIdx= :lastIdx )) " +
            "and (b.boardIdx not in :lastIdx)) ))" +
            //"b.boardIdx not in :lastIdx "+
            "order by b.cnt desc, b.createAt desc ,b.boardIdx desc")
    Page<Board> findAllByCategoryIdxAndLikedWithLastIdx(@Param("category_idx") Long idx,
                                             @Param("lastIdx") Long lastArticleIdx,
                                             Pageable pageable);


    /**
     * 좋아요순(조회수) + 최초 조회
     * [조건] : lastArticleIdx 필요 x
     **/
    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx " +
            "order by b.cnt desc, b.createAt desc ,b.boardIdx desc")
    Page<Board> findAllByCategoryIdxAndLiked(@Param("category_idx") Long idx,
                                             Pageable pageable);


    /**
     * 최신순
     * [조건] : lastArticleIdx 필요
     **/
    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx " +
            "and b.boardIdx < :lastIdx " +
            "order by b.createAt desc, b.boardIdx desc")
    Page<Board> findAllByCategoryIdxAndCreateAtWithLastIdx(@Param("category_idx") Long idx,
                                                @Param("lastIdx") Long lastArticleIdx,
                                                Pageable pageable);
    /**
     * 최신순 + (최초 조회) + 모집중만 추가
     * [조건] : lastArticleIdx 필요 x
    **/

    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx " +
            "order by b.createAt desc, b.boardIdx desc")
    Page<Board> findAllByCategoryIdxAndCreateAt(@Param("category_idx") Long idx,
                                                Pageable pageable);




    /**
     * 기술스택 검색 + 좋아요순(조회수) + (최초 조회)
     * [조건] : lastArticleIdx 필요 x
     **/
    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx " +
            "and b.boardIdx in (:boardIdxst) " +
            "order by b.cnt desc, b.createAt desc ,b.boardIdx desc")
    Page<Board> findAllByCategoryIdxAndLikedAndStacks(@Param("category_idx") Long idx,
                                                      @Param("boardIdxst") List<Long> boardIdxst,
                                                      Pageable pageable);



    /**
     * 참고) https://wikidocs.net/155529
     * 기술스택 검색 + 좋아요순(조회수)
     * [조건] : lastArticleIdx 필요 x
     *
     *             @Query(value = "select b " +
     *             "from Board b " +
     *             "where b.category.idx = :category_idx " +
     *             "and b.boardIdx in (:boardIdxst) " +
     *             //"and b.boardIdx < :lastIdx " +
     *             "and b.cnt <= (select bc.cnt from Board bc where bc.boardIdx= :lastIdx ) " +
     *             "and b.boardIdx not in :lastIdx " +
     *             "order by b.cnt desc, b.createAt desc ,b.boardIdx desc")
     **/
    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx " +
            "and b.boardIdx in (:boardIdxst) " +
            "and " +
            " ((( b.cnt = (select bc.cnt from Board bc where bc.boardIdx= :lastIdx )) " +
            "and (b.boardIdx < :lastIdx)) " +
            "or ((( b.cnt < (select bc.cnt from Board bc where bc.boardIdx= :lastIdx )) " +
            "and (b.boardIdx not in :lastIdx)) ))" +
            "order by b.cnt desc, b.createAt desc ,b.boardIdx desc")
    Page<Board> findAllByCategoryIdxAndLikedWithLastIdxAndStacks(@Param("category_idx") Long idx,
                                                        @Param("lastIdx") Long lastArticleIdx,
                                                        @Param("boardIdxst") List<Long> boardIdxst,
                                                        Pageable pageable);





    /**
     * 최신순 + (최초 조회)
     * [조건] : lastArticleIdx 필요 x
     **/

    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx " +
            "and b.boardIdx in (:boardIdxst) " +
            "order by b.createAt desc, b.boardIdx desc")
    Page<Board> findAllByCategoryIdxAndCreateAtAndStacks(@Param("category_idx") Long idx,
                                                         @Param("boardIdxst") List<Long> boardIdxst,
                                                         Pageable pageable);


    /**
     * 기술스택 검색 + 최신순
     * [조건] : lastArticleIdx 필요
     **/
    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx " +
            "and b.boardIdx < :lastIdx " +
            "and b.boardIdx in (:boardIdxst) " +
            "order by b.createAt desc, b.boardIdx desc")
    Page<Board> findAllByCategoryIdxAndCreateAtWithLastIdxAndStacks(@Param("category_idx") Long idx,
                                                                    @Param("lastIdx") Long lastArticleIdx,
                                                                    @Param("boardIdxst") List<Long> boardIdxst,
                                                           Pageable pageable);


    //기술 스택 검색
    //findByStackNameContaining(String stackName)

    //'기준 컬럼명'으로 정렬해 결과 반환
    //List<Entity명> list명 = repository명.findAll(Sort.by(Sort.Direction.DESC/ASC, "기준컬럼명"));
}
