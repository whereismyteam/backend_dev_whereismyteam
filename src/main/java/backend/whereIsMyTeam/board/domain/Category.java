package backend.whereIsMyTeam.board.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@Table(name = "CATEGORYS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_idx",nullable = false, unique = true)
    private Long categoryIdx;

    @Column(name = "category_name" ,nullable = false, unique = true)
    private String categoryName;

    @Column(nullable = false, length=2)
    @ColumnDefault("'Y'")
    private String status;

    @OneToMany(mappedBy = "category")
    private List<CategoryBoard> boards = new ArrayList<>();
}
