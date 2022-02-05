package backend.whereIsMyTeam.study;

import lombok.RequiredArgsConstructor;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudyController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
}
