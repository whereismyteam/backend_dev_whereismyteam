package backend.whereIsMyTeam.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
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

    @ManyToOne
    @JoinColumn(name = "board_idx")
    private Board board;

    //오류발생 매핑 문제?
    @ManyToOne
    @JoinColumn(name = "stack_idx")
    private TechStack techStack;
}
