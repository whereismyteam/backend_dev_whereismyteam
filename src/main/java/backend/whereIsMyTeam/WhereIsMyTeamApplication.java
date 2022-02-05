package backend.whereIsMyTeam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
@MapperScan(basePackages = "org.springframework.security.crypto.password")
public class WhereIsMyTeamApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhereIsMyTeamApplication.class, args);
	}

}
