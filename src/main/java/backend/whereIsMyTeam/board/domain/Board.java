package backend.whereIsMyTeam.board.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
import backend.whereIsMyTeam.exception.Board.WrongInputException;
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
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@Table(name = "BOARDS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    /**
     *
     * 1. Table명 : 게시글
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

    //글 제목
    @Column(nullable = false, length = 100)
    private String title;

    //글 내용
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     *  필드: '게시글 조회 수'
     *  조건) ['인기순' 나열 기준]
     *  기능 요구사항: 클릭 시 중복되지 않도록 방법이 필요
    **/
    @Column(name = "view_cnt",nullable = false)
    @ColumnDefault("0")
    private Long cnt;

    //필드: '상태' _(임시저장, 모집중, 삭제,모집완료)
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(name="b_status",nullable = false)
    private List<BoardStatus> boardStatuses = new ArrayList<>();


    //필드: '회의방식' _ (온/온오프/오프라인) 중 1택
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(name="b_meetingStatus",nullable = false)
    private List<MeetingStatus> meetingStatuses = new ArrayList<>();


    //필드: '분야'(카테고리) _ (프로젝트,대회,스터디)
    //다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_idx")
    private Category category;


    // 필드: '모집인원'
    @ColumnDefault("0")
    private Long capacityNum;


    //필드: '모집파트'(태그)
    @Column(nullable = false)
    @ElementCollection(targetClass = String.class)
    private List<String> recruitmentPart = new ArrayList<>();


    //다대일 양방향 게시글(n): 유저(1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_idx")
    @OnDelete(action = OnDeleteAction.CASCADE) //작성자가 탈퇴시, 글도 삭제해주자
    private User writer;

    //필드: 스택_게시판 (조인테이블)
    // 다대일 일대다 양방향
    @OneToMany(mappedBy = "board")
    private List<TechStackBoard> techstacks = new ArrayList<>();



    //필드: '지역'
    // 다대일 일대다 양방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_board_idx")
    private Area area;




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

    // 필드: '댓글'
    //게시글을 삭제하면 달려있는 댓글 모두 삭제
    @OneToMany(mappedBy = "board", cascade =CascadeType.ALL , orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    //연관관계 편의 메서드
    public void addComment(Comment comment){
        //comment의 Board 설정은 comment에서 함
        commentList.add(comment);
    }

    public void setHitCnt(Long cnt){
        this.cnt=cnt;
    }
    public Long getHitCnt(){
        return this.cnt;
    }

    public void setBoardStatuses(String status){
        switch (status){
            case "모집중":
                if(!this.getBoardStatuses().get(0).getStatus().equals("모집중"))
                    this.getBoardStatuses().set(0,BoardStatus.RECRUITED);
                else
                    throw new WrongInputException();
                break;
            case "모집완료":
                if(!this.getBoardStatuses().get(0).getStatus().equals("모집완료"))
                    this.getBoardStatuses().set(0,BoardStatus.COMPLETED);
                else
                    throw new WrongInputException();
                break;
            case "임시저장":
                if(!this.getBoardStatuses().get(0).getStatus().equals("임시저장"))
                    this.getBoardStatuses().set(0,BoardStatus.STORED);
                else
                    throw new WrongInputException();
                break;
            default: //삭제
                if(!this.getBoardStatuses().get(0).getStatus().equals("삭제"))
                    this.getBoardStatuses().set(0,BoardStatus.REMOVED);
                else
                    throw new WrongInputException();
                break;
        }
    }



}
