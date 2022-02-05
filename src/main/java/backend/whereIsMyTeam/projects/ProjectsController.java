package backend.whereIsMyTeam.projects;

import lombok.RequiredArgsConstructor;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectsController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
}
