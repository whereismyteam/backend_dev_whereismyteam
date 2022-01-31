package backend.whereIsMyTeam.contest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) //읽기전용 클래스
@RequiredArgsConstructor
public class ContestService {
}
