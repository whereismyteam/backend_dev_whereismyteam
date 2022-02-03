package backend.whereIsMyTeam.study;

import lombok.RequiredArgsConstructor;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) //읽기전용 클래스
@RequiredArgsConstructor
public class StudyService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
}
