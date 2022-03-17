package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.dto.MainBoardListResponseDto;

import backend.whereIsMyTeam.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository <Board, Long> {

    Optional<Board> findByBoardIdx(long boardIdx);

    List<Board> findByWriter(User user);

    @EntityGraph(attributePaths = {"writer"})
    Optional<Board> findWithWriterByBoardIdx(Long userIdx);

    //https://ykh6242.tistory.com/105 참고
    //네이티브 SQL x
    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx " +
            //"and b.boardStatuses = BoardStatus.RECRUITED  " +
            "order by b.createAt desc , b.cnt desc"
            //"and "
    )
    List<Board> findAllByCategoryIdx(@Param("category_idx") Long idx);

    /**
     * 기술스택_ 조회를 위해
     * 참고) https://wikidocs.net/155529
    **/
    //모집중만 띄워주는 쿼리
//    @Query(value = "SELECT b " +
//            "FROM boards as b " +
//            "LEFT JOIN users as u " +
//            "ON b.user_idx = u.userIdx " +
//            "LEFT JOIN categoryss as c " +
//            "ON b.category_idx = c.idx " +
//            "WHERE (b.category_idx = :category_idx)  " +
//            "AND (b.user_idx = :user_idx) " +
////            "where b.category.idx=:category_idx and b.user.userIdx=:user_idx " +
////            "and b.boardStatuses = BoardStatus.RECRUITED " +
//            "ORDER BY b.createAt DESC ",
//            nativeQuery = true)
//    List<Board> findAllByCategoryIdxWithBoardStatus(@Param("category_idx") Long idx
//            , @Param("user_idx")Long userIdx);

    //모집중만 띄워주는 쿼리
    //List<Board> findAllByCategoryIdxWithoutBoardStatus(@Param("category_idx") Long idx);

    //기술 스택 검색
    //findByStackNameContaining(String stackName)

//    @Query("select distinct Board " +
//            "from Feed as feed " +
//            "join fetch feed.author " +
//            "where feed.id <= :feedId and feed.step in :steps " +
//            "order by feed.createdDate desc, feed.id desc")
//    List<Board> findWithoutHelp(@Param("steps") EnumSet<Step> steps, @Param("feedId") Long feedId, Pageable pageable);

    //'기준 컬럼명'으로 정렬해 결과 반환
    //List<Entity명> list명 = repository명.findAll(Sort.by(Sort.Direction.DESC/ASC, "기준컬럼명"));
}
