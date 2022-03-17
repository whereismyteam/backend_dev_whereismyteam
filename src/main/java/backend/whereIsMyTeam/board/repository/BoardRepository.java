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
    //좋아요순(조회수) 쿼리
    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx " +
            "order by b.cnt desc, b.createAt desc "
    )
    List<Board> findAllByCategoryIdxAndLiked(@Param("category_idx") Long idx);

    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx " +
            "order by b.createAt desc"
    )
    List<Board> findAllByCategoryIdxAndCreateAt(@Param("category_idx") Long idx);


    //좋아요순,최신순 x 경우
    @Query(value = "select b " +
            "from Board b " +
            "where b.category.idx = :category_idx "
    )
    List<Board> findAllByCategoryIdx(@Param("category_idx") Long idx);

    /**
     * 기술스택_ 조회를 위해
     * 참고) https://wikidocs.net/155529
    **/

    //기술 스택 검색
    //findByStackNameContaining(String stackName)

    //'기준 컬럼명'으로 정렬해 결과 반환
    //List<Entity명> list명 = repository명.findAll(Sort.by(Sort.Direction.DESC/ASC, "기준컬럼명"));
}
