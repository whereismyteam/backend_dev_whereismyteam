package backend.whereIsMyTeam.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_idx",nullable = false, unique = true)
    private Long profileImgIdx;


    @Column(name = "profile_url",nullable = false)
    private String profileImgUrl;
}
