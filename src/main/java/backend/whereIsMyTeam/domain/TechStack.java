package backend.whereIsMyTeam.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class TechStack extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stack_idx",nullable = false, unique = true)
    private Long stackIdx;

    @Column(nullable = false)
    private String stackName;

    @OneToMany(mappedBy = "techStack")
    private List<TechStackBoard> boards = new ArrayList<>();
}
