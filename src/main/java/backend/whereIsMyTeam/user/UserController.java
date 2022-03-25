package backend.whereIsMyTeam.user;

//import backend.whereIsMyTeam.oauth.AuthCode;
import backend.whereIsMyTeam.exception.Jwt.*;
import backend.whereIsMyTeam.exception.User.AuthCodeNotExistException;
import backend.whereIsMyTeam.exception.User.GoToEmailAuthException;
import backend.whereIsMyTeam.exception.User.OnlyUserCanUseException;
import backend.whereIsMyTeam.exception.User.UserNotExistException;
import backend.whereIsMyTeam.oauth.AuthCode;
import backend.whereIsMyTeam.redis.RedisService;
import backend.whereIsMyTeam.redis.domain.RedisKey;
import backend.whereIsMyTeam.security.jwt.JwtTokenProvider;
import backend.whereIsMyTeam.user.domain.User;
import backend.whereIsMyTeam.result.SingleResult;
import backend.whereIsMyTeam.user.service.ResponseService;
import backend.whereIsMyTeam.user.service.UserService;
import backend.whereIsMyTeam.user.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final UserRepository userRepository;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
    * 로컬 로그인 API
     *
     *
    **/
    @PostMapping("/login")
    public SingleResult<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto requestDto){
        UserLoginResponseDto responseDto = userService.loginUser(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    /**
     * 소셜 로그인(구글) API
     * [POST]  /users/login/:provider
     *
     **/
    @PostMapping("/login/{provider}")
    public SingleResult<UserLoginResponseDto> loginByGoogle(@RequestBody String authCode, @PathVariable String provider) {
        if(authCode==null)
            throw new AuthCodeNotExistException();
        //UserLoginResponseDto responseDto = userService.loginUserByProvider(authCode, provider);
        UserLoginResponseDto responseDto = userService.loginUserByProvider("4/0AX4XfWg5ooH-oy_Zx-OAaCho6YRoqnzFzfutyqEj3rSHHcIrYhYkuHhjo9tKxIDdH6tsug", provider);
        return responseService.getSingleResult(responseDto);
    }


    /**
     * Refresh 토큰 이용해 Access 토큰 재발급 API
     * [POST] /users/newAccessToken
     * param
     * @return SingleResult<TokenResponseDto>
     */
    @PostMapping("/newAccessToken")
    public SingleResult<ReIssueResponseDto> reIssue(HttpServletRequest header , @Valid @RequestBody ReIssueRequestDto requestDto ){

        //리프레시 토큰 null 아닌지 검증
        String refreshToken=jwtTokenProvider.resolveRefreshToken(header);
        if(refreshToken==null)
            throw new RefreshNotExistException();

        //리프레시 토큰 유효 한지 검증
        User user=userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new);
        String findRefreshToken = redisService.getData(RedisKey.REFRESH.getKey()+user.getEmail());
        if ( findRefreshToken==null)
            throw new GoToLoginException();
        if(!findRefreshToken.equals(refreshToken))
            throw new InvalidRefreshTokenException();

        //리프레시 토큰 expiration 유효한지 검증, 유효 o->service단에서 access 토큰 재발급, 유효 x->오류처리(로그인 해주세요)
        if(jwtTokenProvider.validateTokenExpiration(refreshToken))
            throw new GoToLoginException();

        //access 토큰 재발행
        ReIssueResponseDto responseDto = userService.reIssue(requestDto);

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
        userService.confirmNewNickName(requestDto);

        return responseService.getSingleResult("사용 가능한 닉네임입니다.");
    }

    /**
     * 로그아웃 API
     * [POST] /users/logout
     * @return SingleResult<String>
     */
    @PostMapping("/logout")
    public SingleResult<String> logout ( HttpServletRequest header, @Valid @RequestBody UserLogoutRequestDto requestDto) {

        if(requestDto.getUserIdx()!=0) {
            //회원 유저인덱스 일치 검증
            User user = userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new);

            //access token 검증
            jwtTokenProvider.validateAccess(header, user.getEmail());

            //access 토큰 문제 없으므로 로그아웃 진행
            userService.logout(header,requestDto);

            return responseService.getSingleResult("로그아웃 됐습니다.");
        }
        else //유저가 아니므로 사용 불가, 오류 처리
            throw new OnlyUserCanUseException();
    }

    /**
     * 유저 정보 조회 API
     **/
    @GetMapping("/{userIdx}/info")
    public SingleResult<GetUserInfoResDto> userInfo (HttpServletRequest header, @PathVariable long userIdx){

        if(userIdx!=0) {
            //회원 유저인덱스 일치 검증
            User user = userRepository.findByUserIdx(userIdx).orElseThrow(UserNotExistException::new);
            //access token 검증
            jwtTokenProvider.validateAccess(header, user.getEmail());
            //검증 끝났으므로 로직 진행
            GetUserInfoResDto responseDto = userService.userInfo(user);

            return responseService.getSingleResult(responseDto);
        }
        else //유저가 아니므로 사용 불가, 오류 처리
            throw new OnlyUserCanUseException();
    }

}
