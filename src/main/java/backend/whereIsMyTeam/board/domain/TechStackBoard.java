package backend.whereIsMyTeam.board.domain;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.domain.TechStack;
import backend.whereIsMyTeam.config.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStackBoard extends BaseTimeEntity {

    //조인테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stackBoard_idx",nullable = false, unique = true)
    private Long stackBoardIdx;

    @Column(nullable = false, length=2)
    @ColumnDefault("'Y'")
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_idx")
    private Board board;

    //오류발생 매핑 문제?
   // @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_idx")
    private TechStack techStack;
}
