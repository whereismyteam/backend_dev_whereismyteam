package backend.whereIsMyTeam.domain;

//지역 테이블

import backend.whereIsMyTeam.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "AREAS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Area extends BaseTimeEntity {

    //값 17개 중 1택
    //기타/제주/순천/원주/청주/천안/세종/대전/전주/광주/울산/부산/대구/인천/수원/서울/수도권/전국

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_idx",nullable = false, unique = true)
    private Long areaIdx;

    @Column(name = "area_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "area")
    private List<Board> board= new ArrayList<>();

}
