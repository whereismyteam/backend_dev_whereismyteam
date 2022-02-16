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
public class CategoryBoard {

    //조인테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryBoard_idx",nullable = false, unique = true)
    private Long categoryIdx;

    @Column(nullable = false, length=2)
    @ColumnDefault("'Y'")
    private String status;

    @ManyToOne
    @JoinColumn(name = "board_idx")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "category_idx")
    private Category category;
}
