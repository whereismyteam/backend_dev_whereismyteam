package backend.whereIsMyTeam.board.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
import backend.whereIsMyTeam.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@DynamicInsert
@Table(name = "COMMENTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    /**
     * 1. Table 명: 댓글
     * 댓글과 유저는 '다대일 관계'
     * 2. 조건)
     * - 한 개의 게시글에는 여러 개의 댓글 ㅇ
     * - 한 명의 사용자는 여러 개의 댓글을 작성할 수 ㅇ
     **/

    //댓글 idx
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_idx")
    private Long commentIdx;

    // 댓글 내용
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    //비밀 댓글 여부
    @Column(name = "is_secret", nullable = false)
    private String isSecret;

    //다대일 양방향 (댓글N:유저1 )
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_idx")
    private User user; // 작성자

    //삭제인지아닌지
    @Column(nullable = false, length=2)
    @ColumnDefault("'Y'")
    private String status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_idx")
    private Board board;




    /**
     * 해당 블로그 도메인 참조
     * https://ttl-blog.tistory.com/278?category=910686
     * **/
    //댓글 (연관관계 주인)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_idx")
    private Comment parent;

    //== 부모 댓글을 삭제해도 자식 댓글은 남아있음 ==//
    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();

    //댓글 또는 답글 삭제
    public void delete() {
        this.status = "N";
    }


    //연관관계 편의 메서드
    public void confirmWriter(User writer) {
        this.user = writer;
        writer.addComment(this);
    }

    public void confirmBoard(Board board) {
        this.board = board;
        board.addComment(this);
    }

    public void confirmParent(Comment parent){
        this.parent = parent;
        parent.addChild(this);
    }

    public void addChild(Comment child){
        childList.add(child);
    }

    public List<Comment> findRemovableList() {

        List<Comment> result = new ArrayList<>();

        Optional.ofNullable(this.parent).ifPresentOrElse(

                parentComment ->{//답글인 경우 (부모가 존재하는 경우)
                    if( parentComment.status.equals("N")&& parentComment.isAllChildRemoved().equals("Y")){
                        result.addAll(parentComment.getChildList());
                        result.add(parentComment);
                    }
                },

                () -> {//댓글인 경우
                    if (isAllChildRemoved().equals("Y")) {
                        result.add(this);
                        result.addAll(this.getChildList());
                    }
                }
        );

        return result;
    }


    //모든 자식 댓글이 삭제되었는지 판단
    private String isAllChildRemoved() {
        return getChildList().stream()
                .map(Comment::getStatus)//지워졌는지 여부로 바꾼다
                .filter(isRemove -> getStatus().equals("N"))//지워졌으면 true, 안지워졌으면 false
                .findAny()//지워지지 않은게 하나라도 있다면 false를 반환
                .orElse("Y");//모두 지워졌다면 true를 반환
    }


    @Builder
    public Comment(String content,String isSecret,Comment parent,User user,Board board){
        this.content=content;
        this.isSecret=isSecret;
        this.parent=parent;
        this.user=user;
        this.board=board;
    }

}
