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
import java.util.List;

@Entity
@Getter
@DynamicInsert
@Table(name = "Board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {


    //필드에 들어가야 하는 것들:
    // 제목,글 내용, 조회수,분류(카테고리), 모집인원, 상태(모집중,모집완료,임시저장[게시 안된 상태],삭제상태)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long boardIdx;

    @Column(nullable = false, length = 100)//제목
    private String title;

    //글 내용
    @Column
    private String content;

    //작성자(닉네임)
    @Column
    private String nickname;

    //조회 수
    @Column
    private Long cnt;

//    @Column
//    private category;
//
//    //유저 부분
//    @Column
//    private User user;



//    @Builder
//    public Board(String email, String password, String provider, String nickName, List<Role> roles) {
//        //this.status=status;
//        this.email = email;
//        this.password = password;
//        this.nickName = nickName;
//        this.provider = provider;
//        //this.emailAuth = emailAuth;
//        //this.profileImgIdx=profileImgIdx;
//        this.roles = roles;
//    }
}
