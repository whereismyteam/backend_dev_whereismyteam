package backend.whereIsMyTeam.contest;

import lombok.RequiredArgsConstructor;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContestService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
}
