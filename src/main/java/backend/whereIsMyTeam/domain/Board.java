package backend.whereIsMyTeam.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
import backend.whereIsMyTeam.user.domain.Role;
import backend.whereIsMyTeam.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@Table(name = "BOARDS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {


    //필드에 들어가야 하는 것들:
    // 제목,글 내용, 조회수,분류(카테고리), 모집인원, 상태(모집중,모집완료,임시저장[게시 안된 상태],삭제상태)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="board_Idx",nullable = false, unique = true)
    private Long boardIdx;

    //제목
    @Column(nullable = false, length = 100)
    private String title;

    //글 내용
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    //작성자(닉네임) _User 객체에서 참조
//    @Column
//    private String nickname;

    //게시글 조회 수
    @Column
    private Long cnt;

    //글 상태 4가지 중 1개 택 _BoardStatus
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private BoardStatus status;


    //회의방식 (온/온오프/오프라인)



    //댓글
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Comment> comments;







    //게시글 작성 Builder
    @Builder
    public Board(String title, BoardStatus stauts, String content) {
        this.title=title;
        this.status=status;
        this.content = content;
    }


}
