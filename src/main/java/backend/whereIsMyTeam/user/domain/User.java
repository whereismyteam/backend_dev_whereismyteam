package backend.whereIsMyTeam.user.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.domain.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;


@Entity
@Getter
@DynamicInsert
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long userIdx;

    @Column(nullable = false, length=2)
    @ColumnDefault("'Y'")
    private String status;

//    @Column(nullable = false)
//    @ColumnDefault("1")
//    private Long profileImgIdx;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length=255)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean emailAuth;


    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    @Column
    private String provider;

    //프로필 이미지(n) -  (1)유저 -> 유저기준 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="profile_idx")
    private ProfileImages profileImages;


    //== 회원탈퇴 -> 작성한 게시물, 댓글 모두 삭제 ==//
    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();



    //String provider
    @Builder
    public User(String email, String password, String provider, String nickName,List<Role> roles) {
        //this.status=status;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.provider = provider;
        //this.emailAuth = emailAuth;
        //this.profileImgIdx=profileImgIdx;
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    //public void changeRole() { this.roles.set(this.getUserIdx().intValue(),Role.ROLE_AUTH); }
    //public void changeEmailAuth() { this.emailAuth=true;}
    public void emailVerifiedSuccess(List<Role> rolee) {
        this.roles=rolee;
        this.emailAuth = true;
    }

    //== 연관관계 메서드 ==//
    public void addComment(Comment comment){
        //comment의 writer 설정은 comment에서 함
        commentList.add(comment);
    }


}

