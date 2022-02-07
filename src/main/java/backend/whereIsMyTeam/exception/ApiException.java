package backend.whereIsMyTeam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class ApiException { // 예외 기본형태 (메세지,상태코드,발생시긴)
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}
