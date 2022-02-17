package backend.whereIsMyTeam.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
import backend.whereIsMyTeam.user.domain.Role;
import backend.whereIsMyTeam.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@Table(name = "BOARDS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    /**
     * [필드에 들어가야 하는 것들]
     *   : 제목,글 내용, 조회수,분류(카테고리), 모집인원, 상태(모집중,모집완료,임시저장[게시 안된 상태],삭제상태)
     * [테이블 생성 할 것]
     *  : 지역,스택,스택_게시글, 모집파트_게시글,즐겨찾기(찜), 모집파트(추가가능 하도록=태그기능?)
     *
     **/


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="board_idx",nullable = false, unique = true
            ,insertable=false, updatable=false) //조인 사용 오류로 추가 -> 추가/수정 불가능 선언
    private Long boardIdx;

    //제목
    @Column(nullable = false, length = 100)
    private String title;

    //글 내용
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     *  필드: 게시글 조회 수 => ['인기순' 나열 기준]
     *  기능 요구사항: 클릭 시 중복되지 않도록 방법이 필요
    **/
    @Column(name = "board_cnt",nullable = false)
    @ColumnDefault("0")
    private Long cnt;

    //게시글 상태 4가지 중 1개 택
    @Column(name="board_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardStatus boardStatus;


    //회의방식 (온/온오프/오프라인) 중 1택
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MeetingStatus meetingStatus;

    //분류(카테고리)
    //다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_idx")
    private Category category;


    // 모집인원
    @ColumnDefault("0")
    private Long capacityNum;

    @Column(name = "status",nullable = false, length=2)
    @ColumnDefault("'Y'")
    private String status;


    //다대일 양방향 게시글(n): 유저(1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_idx")
    @OnDelete(action = OnDeleteAction.CASCADE) //작성자가 탈퇴시, 글도 삭제해주자
    private User writer;

    //다대일 일대다 양방향
    @OneToMany(mappedBy = "board")
    private List<TechStackBoard> techstacks = new ArrayList<>();

    //다대일 일대다 양방향
    @OneToMany(mappedBy = "boards")
    private List<CategoryBoard> categorys = new ArrayList<>();

    //Board-Area다대일 일대다 양방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_idx")
    private Area area;

    @Column(name = "recruitment_field", nullable = false)
    @ElementCollection(targetClass = String.class)
    private List<String> recruitmentField = new ArrayList<>();


    //게시글 작성 Builder
    @Builder
    public Board(String title,Area area, Long capacityNum, String content
            , User writer, Category category, BoardStatus boardStatus) {
        this.title=title;
        this.category = category;
        this.capacityNum = capacityNum;
        //this.boardStatus = status;
        this.content = content;
        this.writer = writer;
    }


}
