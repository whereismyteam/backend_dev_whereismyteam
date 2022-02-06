package backend.whereIsMyTeam.contest;

import lombok.RequiredArgsConstructor;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContestController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
}
