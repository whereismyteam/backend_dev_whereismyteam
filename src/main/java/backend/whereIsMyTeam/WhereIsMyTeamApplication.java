package backend.whereIsMyTeam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
public class WhereIsMyTeamApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhereIsMyTeamApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
