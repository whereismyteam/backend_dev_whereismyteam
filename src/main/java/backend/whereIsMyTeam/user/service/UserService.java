package backend.whereIsMyTeam.user.service;

import backend.whereIsMyTeam.email.EmailService;
import backend.whereIsMyTeam.exception.Jwt.InvalidRefreshTokenException;
import backend.whereIsMyTeam.exception.User.*;
import backend.whereIsMyTeam.redis.domain.RedisKey;
import backend.whereIsMyTeam.redis.RedisService;
import backend.whereIsMyTeam.redis.dto.ReIssueRequestDto;
import backend.whereIsMyTeam.security.jwt.JwtTokenProvider;
import backend.whereIsMyTeam.user.UserRepository;
import backend.whereIsMyTeam.user.domain.Role;
import backend.whereIsMyTeam.user.domain.User;
import backend.whereIsMyTeam.user.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import backend.whereIsMyTeam.security.dto.TokenResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RedisService redisService;

    private final EmailService emailService;


    /*
    * LoginRequestDto로 들어온 값을 통해 로그인 진행
    * 로컬 로그인
     */
    @Transactional
    public UserLoginResponseDto loginUser(@RequestBody @Valid UserLoginRequestDto requestDto) {
        //Email로 실제 저장된 유저인지 확인
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        //패스워드 오류 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword()))
            throw new LoginFailureException();

        //토큰 발급 (Access, Refresh)
        String refreshToken = jwtTokenProvider.createRefreshToken();

        redisService.setDataWithExpiration(RedisKey.REFRESH.getKey()
                +user.getEmail(), refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        return new UserLoginResponseDto(user.getUserIdx(), jwtTokenProvider.createToken(requestDto.getEmail()), refreshToken);

//        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());
//
//        //입력한 로그인 정보(이메일) 회원조회
//        if(user.isEmpty())  //유저 존재여부
//            throw new UserNotFoundException();
//
//        boolean pwdCorrect= !(passwordEncoder.matches(requestDto.getPassword(),
//                user.get().getPassword()));
//
//        if (pwdCorrect==true)
//            throw new LoginFailureException();
//
//
//        String refreshToken = jwtTokenProvider.createRefreshToken();
//
//        //redis에 key-value, 만료기간 저장
//        redisService.setDataWithExpiration(RedisKey.REFRESH.getKey()
//                        + user.get().getEmail(),
//                refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);
//
//        return new UserLoginResponseDto(user.get().getUserIdx(),
//                jwtTokenProvider.createToken(requestDto.getEmail()), refreshToken);


    }






    /**
     * Refresh 토큰으로 토큰 재발행
     * @param requestDto
     * @return
     */
    @Transactional
    public TokenResponseDto reIssue(@RequestBody @Valid ReIssueRequestDto requestDto) {

        //KEY값 이용해 Refresh 토큰값 읽어오기
        String findRefreshToken = redisService.getData(RedisKey.REFRESH.getKey()+requestDto.getEmail());

        //Redis에서 꺼내온 값이 없음 or 사용자가 가져온 Refresh토큰값이 Redis랑 다르다-> 예외처리
        //유효한 refresh 토큰이 아님
        if (findRefreshToken == null || !findRefreshToken.equals(requestDto.getRefreshToken()))
            throw new InvalidRefreshTokenException();

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        //Access 토큰, Refresh 토큰 재발급
        String accessToken = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        //바뀐 토큰값을 Redis에 key-value 재설정
        redisService.setDataWithExpiration(RedisKey.REFRESH.getKey() + user.getEmail(),
                refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    /**
     * Dto로 들어온 값을 통해 회원가입을 진행
     * @param requestDto
     * @return
     */
    @Transactional
    public UserRegisterResponseDto registerUser(UserRegisterRequestDto requestDto) {

        // String authToken = UUID.randomUUID().toString();
        //유효 시간 5분
        //key+이메일, 전달 데이터로
        // redisService.setDataWithExpiration(RedisKey.EAUTH.getKey()+requestDto.getEmail(), authToken, 60*5L);

        //이메일과 닉네임 중복을 처리한 후 들어오는 api라 따로 관련 validation 처리 진행x
        //User 객체 저장
        User user = userRepository.save(
                User.builder()
                        .email(requestDto.getEmail())
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .nickName(requestDto.getNickName())
                        .roles(Collections.singletonList(Role.ROLE_NOTAUTH))
                        .provider(null)
                        .build());

        //이메일 전송
        // emailService.send(requestDto.getEmail(), authToken);

        return UserRegisterResponseDto.builder()
                .userIdx(user.getUserIdx())
                .email(user.getEmail())
                .build();
    }


    /**
     * 이메일 인증 성공
     * @param requestDto
     */
    @Transactional
    public void confirmEmail(EmailAuthRequestDto requestDto) {
        //toekn 존재하는지 예외처리
        if (redisService.getData(RedisKey.EAUTH.getKey()+requestDto.getEmail()) == null)
            throw new EmailAuthTokenNotFoundException();
        //이메일 존재하는지 예외처리
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(UserNotExistException::new);
        //이메일 인증 받은 정보로 변환
       // Role role = userRepository.findByUserIdx(user.getUserIdx()).orElseThrow(UserRoleNotExistException::new);
       // user.changeRole();
       // user.changeEmailAuth();
        user.emailVerifiedSuccess(Collections.singletonList(Role.ROLE_AUTH));
        //둘다 존재하면 redis 서버 데이터 지움
        redisService.deleteData(RedisKey.EAUTH.getKey()+requestDto.getEmail());
    }

    /**
     * 이메일 중복 체크
     * @param requestDto
     */
    public void confirmNewEmail(NewEmailRequestDto requestDto) {
        //예외 런타임오류 상속하는 거 맞는지
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent())
            throw new UserEmailAlreadyExistsException();
    }

    /**
     * 닉네임 중복 체크
     * @param requestDto
     */
    public void confirmNewNickName(NewNickNameRequestDto requestDto) {
        //예외 런타임오류 상속하는 거 맞는지
        if (userRepository.findByNickName(requestDto.getNickName()).isPresent())
            throw new UserNickNameAlreadyExistsException();
    }
    /*public void confirmNewNickName(String nickName) {
        //예외 런타임오류 상속하는 거 맞는지
        if (userRepository.findByNickName(nickName).isPresent())
            throw new UserNickNameAlreadyExistsException();
    }*/

    /**
     * 이메일 인증 링크 전송
     * @param requestDto
     */
    @Transactional
    public void sendEmail(NewEmailRequestDto requestDto) {

        //가입하지않은 유저 오류 처리
        if (userRepository.findByEmail(requestDto.getEmail()).isEmpty())
            throw new UserNotExistException();

        String authToken = UUID.randomUUID().toString();
        //유효 시간 5분
        //key+이메일, 전달 데이터로
        redisService.setDataWithExpiration(RedisKey.EAUTH.getKey()+requestDto.getEmail(), authToken, 60*5L);
        //이메일 전송
        emailService.send(requestDto.getEmail(), authToken);
    }

}
