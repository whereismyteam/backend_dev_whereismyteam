package backend.whereIsMyTeam.user.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Entity
@Getter
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false, unique = true)
        private Long userIdx;

        @Enumerated(EnumType.STRING)
        private UserStatus status;

        //ProfileImg 도메인 생성 후, 연관관계 여부따라 (추가하기로)
        private Long profileImgIdx;

        @Column( nullable = false)
        private String email;

        @Column(nullable = false, length=30)
        private String password;

        @Column(nullable = false)
        private String nickName;

        private Boolean emailAuth;


        @ElementCollection(fetch = FetchType.LAZY)
        @Enumerated(EnumType.STRING)
        private List<Role> roles = new ArrayList<>();

        @Builder
        public User(String email, String password, String nickName,String provider, List<Role> roles,Long profileImgIdx ,Boolean emailAuth) {
            this.email = email;
            this.password = password;
            this.nickName = nickName;
            //this.provider = provider;
            //this.roles = Collections.singletonList(Role.ROLE_USER);
            this.emailAuth = emailAuth;
            this.profileImgIdx=profileImgIdx;
        }

        public void addRole(Role role) {
            this.roles.add(role);
        }

        public void emailVerifiedSuccess() {
            this.emailAuth = true;
        }


}
