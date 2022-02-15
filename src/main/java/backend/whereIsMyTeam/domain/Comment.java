package backend.whereIsMyTeam.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
import backend.whereIsMyTeam.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
//@Builder
@Table(name = "Comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    /**
     *
     * 댓글과 유저는 '다대일 관계'
     * 한 개의 게시글에는 여러 개의 댓글 ㅇ
     * 한 명의 사용자는 여러 개의 댓글을 작성할 수 ㅇ
     *
     **/

    //댓글 idx
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_idx")
    private Long commentIdx;

    // 댓글 내용
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    //다대일 양방향 (댓글N:유저1 )
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_idx")
    private User user; // 작성자


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

}
