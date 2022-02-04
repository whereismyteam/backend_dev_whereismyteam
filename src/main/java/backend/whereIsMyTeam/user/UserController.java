package backend.whereIsMyTeam.user;

import backend.whereIsMyTeam.redis.dto.ReIssueRequestDto;
import backend.whereIsMyTeam.result.SingleResult;
import backend.whereIsMyTeam.security.dto.TokenResponseDto;
import backend.whereIsMyTeam.user.dto.UserLoginRequestDto;
import backend.whereIsMyTeam.user.dto.UserLoginResponseDto;
import backend.whereIsMyTeam.user.service.ResponseService;
import backend.whereIsMyTeam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
    * 로컬 로그인 API
    */
    @PostMapping("/login")
    public SingleResult<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto requestDto) {
        UserLoginResponseDto responseDto = userService.loginUser(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    /*
    * 'Refresh 토큰' 이용해 토큰 재발급 API
    */
    @PostMapping("/newAccessToken")
    public SingleResult<TokenResponseDto> reIssue(@RequestBody ReIssueRequestDto requestDto) {
        TokenResponseDto responseDto = userService.reIssue(requestDto);
        return responseService.getSingleResult(responseDto);
    }
}
