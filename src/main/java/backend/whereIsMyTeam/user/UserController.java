package backend.whereIsMyTeam.user;

import backend.whereIsMyTeam.user.dto.EmailAuthRequestDto;
import backend.whereIsMyTeam.user.dto.UserRegisterRequestDto;
import backend.whereIsMyTeam.user.dto.UserRegisterResponseDto;
import backend.whereIsMyTeam.result.SingleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 회원 가입 API
     * [GET] /users/signup
     * @return SingleResult<UserRegisterResponseDto>
     */
    @PostMapping("/register")
    public SingleResult<UserRegisterResponseDto> register (@RequestBody UserRegisterRequestDto requestDto) {
        log.info("request = {}, {}, {}", requestDto.getEmail(), requestDto.getPassword(),requestDto.getNickName());
        UserRegisterResponseDto responseDto = userService.registerUser(requestDto);
        return responseService.getSingleResult(responseDto);
    }


    /**
     * 이메일 인증 API
     * [GET] /users/confirm-email
     * @return SingleResult<String>
     */
    @GetMapping("/confirm-email")
    public SingleResult<String> confirmEmail(@ModelAttribute EmailAuthRequestDto requestDto) {
        userService.confirmEmail(requestDto);
        return responseService.getSingleResult("인증이 완료되었습니다.");
    }

}
