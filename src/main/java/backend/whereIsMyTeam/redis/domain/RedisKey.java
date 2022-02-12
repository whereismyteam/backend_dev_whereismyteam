package backend.whereIsMyTeam.redis.domain;

import lombok.Getter;

@Getter
public enum RedisKey {
    REFRESH("REFRESH_"), EAUTH("EAUTH_"),LOGOUT("LOGOUT_");

    private String key;

    RedisKey(String key) {
        this.key = key;
    }

}
