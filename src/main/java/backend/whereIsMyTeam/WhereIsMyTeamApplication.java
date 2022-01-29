package backend.whereIsMyTeam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class WhereIsMyTeamApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhereIsMyTeamApplication.class, args);
	}

}
