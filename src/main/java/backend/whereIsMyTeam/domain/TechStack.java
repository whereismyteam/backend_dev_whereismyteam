package backend.whereIsMyTeam.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "TECHSTACK")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStack extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stack_idx",nullable = false, unique = true)
    private Long stackIdx;

    @Column(nullable = false)
    private String stackName;

    @Column(nullable = false, length=2)
    @ColumnDefault("'Y'")
    private String status;

    @OneToMany(mappedBy = "techStack")
    private List<TechStackBoard> boards = new ArrayList<>();
}
