package backend.whereIsMyTeam.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisService {
    
    private final RedisTemplate redisTemplate;

    //key를 통해 value 값 얻기
    public String getData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    //만료기간 설정
    public void setDataWithExpiration(String key, String value, Long time) {
        if (this.getData(key) != null)
            this.deleteData(key);
        Duration expireDuration = Duration.ofSeconds(time);
        redisTemplate.opsForValue().set(key, value, expireDuration);
    }
    
    //key를 통해 key-value 삭제
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}

