package backend.whereIsMyTeam.user;


import backend.whereIsMyTeam.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository <User, Long>{

}
