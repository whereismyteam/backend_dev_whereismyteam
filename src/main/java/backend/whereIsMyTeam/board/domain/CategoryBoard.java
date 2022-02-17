package backend.whereIsMyTeam.board.domain;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.domain.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryBoard {

    //조인테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryBoard_idx",nullable = false, unique = true)
    private Long categoryIdx;

    @Column(nullable = false, length=2)
    @ColumnDefault("'Y'")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_idx")
    private Board boards;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_idx")
    private Category category;
}