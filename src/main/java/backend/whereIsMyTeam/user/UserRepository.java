package backend.whereIsMyTeam.user;


import backend.whereIsMyTeam.user.domain.Role;
import backend.whereIsMyTeam.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository <User, Long>{

    Optional<User> findByEmail(String email);

    Optional<User> findByNickName(String nickName);

    Optional<Role> findByUserIdx(long userIdx);

    Optional<User> findByIdx(long userIdx);

    Optional<User> findByEmailAndProvider(String email, String provider);
}
