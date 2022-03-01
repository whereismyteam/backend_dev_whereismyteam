package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository <Board, Long> {

    Optional<Board> findByBoardIdx(long boardIdx);

    @EntityGraph(attributePaths = {"writer"})
    Optional<Board> findWithWriterByBoardIdx(Long userIdx);


    List<Board> findAllByCategory(long categoryIdx);

//    @Query("select distinct Board " +
//            "from Feed as feed " +
//            "join fetch feed.author " +
//            "where feed.id <= :feedId and feed.step in :steps " +
//            "order by feed.createdDate desc, feed.id desc")
//    List<Board> findWithoutHelp(@Param("steps") EnumSet<Step> steps, @Param("feedId") Long feedId, Pageable pageable);

    //'기준 컬럼명'으로 정렬해 결과 반환
    //List<Entity명> list명 = repository명.findAll(Sort.by(Sort.Direction.DESC/ASC, "기준컬럼명"));
}
