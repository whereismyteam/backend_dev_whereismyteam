package backend.whereIsMyTeam.board.repository;

import backend.whereIsMyTeam.board.domain.Comment;
import backend.whereIsMyTeam.board.domain.PostLike;
import backend.whereIsMyTeam.board.domain.TechStack;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {


    @Query("select t from TechStack t where t.stackName=:stackName")
    TechStack findByStackName(@Param("stackName") String stackName);



}
