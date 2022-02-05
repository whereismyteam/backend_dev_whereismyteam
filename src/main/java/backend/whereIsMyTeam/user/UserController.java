package backend.whereIsMyTeam.user;

import backend.whereIsMyTeam.exception.User.EmailAuthTokenNotFoundException;
import backend.whereIsMyTeam.exception.User.EmptyNickNameException;
import backend.whereIsMyTeam.exception.User.UserEmailAlreadyExistsException;
import backend.whereIsMyTeam.user.dto.*;
import backend.whereIsMyTeam.result.SingleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
     * [GET] /users/emails/confirm-email
     * @return SingleResult<String>
     */
    @GetMapping("/emails/confirm-email")
    public SingleResult<String> confirmEmail( @Valid @ModelAttribute EmailAuthRequestDto requestDto) {
        userService.confirmEmail(requestDto);
        return responseService.getSingleResult("인증이 완료됐습니다.");
    }

    /**
     * 이메일 중복 체크 API
     * [GET] /users/emails
     * @return SingleResult<String>
     */
    @GetMapping("/emails")
    public SingleResult<String> confirmNewEmail(@Valid @ModelAttribute NewEmailRequestDto requestDto) {
        //이메일 형식 처리 오류 여기서
        //빈 값 들어오는 거 오류 처리
       // if(requestDto.getEmail()==null)
       //     throw new EmptyEmailException();
        userService.confirmNewEmail(requestDto);

        return responseService.getSingleResult("사용 가능한 이메일 입니다.");
    }

    /**
     * 닉네임 중복 체크 API
     * [GET] /users/nickNames
     * @return SingleResult<String>
     */
    @GetMapping("/nickNames")
    public SingleResult<String> confirmNewNickName( @Valid @ModelAttribute NewNickNameRequestDto requestDto) {
        //빈 값 들어오는 거 오류 처리
        //if(requestDto.getNickName()==null)
        //    throw new EmptyNickNameException();

        userService.confirmNewNickName(requestDto);

        return responseService.getSingleResult("사용 가능한 닉네임 입니다.");
    }
   /* @GetMapping("/nickNames/{nickName}")
    public SingleResult<String> confirmNewNickName( @PathVariable String nickName) {
        //빈 값 들어오는 거 오류 처리
        if(nickName==null)
            throw new EmptyNickNameException();

        userService.confirmNewNickName(nickName);

        return responseService.getSingleResult("사용 가능한 닉네임 입니다.");
    }*/


}
