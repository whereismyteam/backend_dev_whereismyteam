package backend.whereIsMyTeam.user;

import backend.whereIsMyTeam.oauth.AuthCode;
import backend.whereIsMyTeam.redis.dto.ReIssueRequestDto;
import backend.whereIsMyTeam.result.SingleResult;
import backend.whereIsMyTeam.security.dto.TokenResponseDto;
import backend.whereIsMyTeam.user.dto.UserLoginRequestDto;
import backend.whereIsMyTeam.user.dto.UserLoginResponseDto;
import backend.whereIsMyTeam.user.service.ResponseService;
import backend.whereIsMyTeam.user.service.UserService;
import backend.whereIsMyTeam.user.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public SingleResult<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto requestDto){
        UserLoginResponseDto responseDto = userService.loginUser(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    /**
     * 소셜 로그인(구글) API
     *
     *
     *
     **/
    @PostMapping("/login/{provider}")
    public SingleResult<UserLoginResponseDto> loginByGoogle(@RequestBody AuthCode authCode,
                                                            @PathVariable String provider) {

        UserLoginResponseDto responseDto = userService.loginUserByProvider(authCode.getCode(), provider);
        return responseService.getSingleResult(responseDto);
    }


    /**
     * 'Refresh 토큰' 이용해 토큰 재발급 API
     * [POST] /users/newAccessToken
     * @return SingleResult<TokenResponseDto>
     */
    @PostMapping("/newAccessToken")
    public SingleResult<TokenResponseDto> reIssue(@RequestBody ReIssueRequestDto requestDto) {
        TokenResponseDto responseDto = userService.reIssue(requestDto);
        return responseService.getSingleResult(responseDto);
    }


    /**
     * 회원 가입 API
     * [POST] /users/signup
     * @return SingleResult<UserRegisterResponseDto>
     */
    @PostMapping("/signup")
    public SingleResult<UserRegisterResponseDto> signup ( @Valid @RequestBody UserRegisterRequestDto requestDto) {
        log.info("request = {}, {}, {}", requestDto.getEmail(), requestDto.getPassword(),requestDto.getNickName());
        UserRegisterResponseDto responseDto = userService.registerUser(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    /**
     * 이메일 링크 전송 API
     * [POST] /users/emails/send-email
     * @return SingleResult<String>
     */
    @PostMapping("/emails/send-email")
    public SingleResult<String> sendEmail (@Valid @RequestBody NewEmailRequestDto requestDto) {
        userService.sendEmail(requestDto);
        return responseService.getSingleResult("메일이 전송됐습니다.");
    }


    /**
     * 이메일 링크 인증 API
     * [GET] /users/emails/confirm-email?email=&authToken=
     * @return SingleResult<String>
     */
    @GetMapping("/emails/confirm-email")
    public SingleResult<String> confirmEmail( @Valid @ModelAttribute EmailAuthRequestDto requestDto) {
        userService.confirmEmail(requestDto);
        return responseService.getSingleResult("인증이 완료됐습니다.");
    }

    /**
     * 이메일 중복 체크 API
     * [GET] /users/emails?email=
     * @return SingleResult<String>
     */
    @GetMapping("/emails")
    public SingleResult<String> confirmNewEmail(@Valid @ModelAttribute NewEmailRequestDto requestDto) {
        //이메일 형식 처리 오류 여기서
        //빈 값 들어오는 거 오류 처리
       // if(requestDto.getEmail()==null)
       //     throw new EmptyEmailException();
        userService.confirmNewEmail(requestDto);

        return responseService.getSingleResult("사용 가능한 이메일입니다.");
    }

    /**
     * 닉네임 중복 체크 API
     * [GET] /users/nickNames?nickName=
     * @return SingleResult<String>
     */
    @GetMapping("/nickNames")
    public SingleResult<String> confirmNewNickName( @Valid @ModelAttribute NewNickNameRequestDto requestDto) {
        //빈 값 들어오는 거 오류 처리
        //if(requestDto.getNickName()==null)
        //    throw new EmptyNickNameException();

        userService.confirmNewNickName(requestDto);

        return responseService.getSingleResult("사용 가능한 닉네임입니다.");
    }

    /**
     * 로그아웃 API
     * [POST] /users/logout
     * @return SingleResult<String>
     */
    @GetMapping("/logout")
    public SingleResult<String> logout ( @Valid @ModelAttribute UserLogoutRequestDto requestDto) {

        userService.logout(requestDto);

        return responseService.getSingleResult("로그아웃 됐습니다.");
    }

}
