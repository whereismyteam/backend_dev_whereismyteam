package backend.whereIsMyTeam.board.domain;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "POSTLIKES") //찜(즐겨찾기)
public class PostLike {

    /**
     * 1. Table 명: '찜(즐겨찾기)'
     * 2. 조건)
     **/

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postLike_idx")
    private Long likeIdx;

    @Column(nullable = false, length=2)
    @ColumnDefault("'Y'")
    private String status;


    /**
     * 좋아요(N) -  유저(1)
     * 다대일 단방향
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")//FK
    private User user;

    /**
     * 좋아요(N) - 게시글(1)
     *  다대일 단방향
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_idx")
    private Board board;

    @Builder
    public PostLike(User user, Board board){
        this.user = user;
        this.board = board;
    }


}
