package backend.whereIsMyTeam.user.domain;

import backend.whereIsMyTeam.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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

        @Column(nullable = false)
        @ColumnDefault("1")
        private Long profileImgIdx;

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


        //String provider
        @Builder
        public User(String email, String password, String nickName,List<Role> roles) {
            //this.status=status;
            this.email = email;
            this.password = password;
            this.nickName = nickName;
            this.roles = roles;
        }

        public void addRole(Role role) {
            this.roles.add(role);
        }

        public void emailVerifiedSuccess() {
            this.emailAuth = true;
        }

}
