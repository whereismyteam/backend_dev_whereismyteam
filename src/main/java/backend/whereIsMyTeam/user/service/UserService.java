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
    public UserLoginResponseDto loginUser(UserLoginRequestDto requestDto) {
        //입력한 로그인 정보(이메일) 회원조회
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        //인코딩 하기 전 PW값 같은지 비교
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword()))
            throw new LoginFailureException();

        //이메일 인증 여부 예외처리
//        if (!user.getEmailAuth())
//            throw new EmailNotAuthenticatedException();

        String refreshToken = jwtTokenProvider.createRefreshToken();

        //redis에 key-value, 만료기간 저장
        redisService.setDataWithExpiration(RedisKey.REFRESH.getKey() + user.getEmail(),
                refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        return new UserLoginResponseDto(user.getUserIdx(),
                jwtTokenProvider.createToken(requestDto.getEmail()), refreshToken);
    }

    //findUserToken()

    /**
     * 토큰 재발행
     * @param requestDto
     * @return
     */
    @Transactional
    public TokenResponseDto reIssue(ReIssueRequestDto requestDto) {

        //Refresh 토큰값 읽어오기
        String findRefreshToken = redisService.getData(RedisKey.REFRESH.getKey()+requestDto.getEmail());

        //refresh 토큰 x or dto에서 불러온 refresh 토큰과 다를경우 예외처리
        if (findRefreshToken == null || !findRefreshToken.equals(requestDto.getRefreshToken()))
            throw new InvalidRefreshTokenException();

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        //Access 토큰, Refresh 토큰 재발급
        String accessToken = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        //Redis에 key-value 재설정
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

        //User 객체 저장
        User user = userRepository.save(
                User.builder()
                        .email(requestDto.getEmail())
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .nickName(requestDto.getNickName())
                        .roles(Collections.singletonList(Role.ROLE_NOTAUTH))
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

        //둘다 존재하면 redis 서버 데이터 지움
        redisService.deleteData(RedisKey.EAUTH.getKey()+requestDto.getEmail());

        user.emailVerifiedSuccess();
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
        String authToken = UUID.randomUUID().toString();
        //유효 시간 5분
        //key+이메일, 전달 데이터로
        redisService.setDataWithExpiration(RedisKey.EAUTH.getKey()+requestDto.getEmail(), authToken, 60*5L);
        //이메일 전송
        emailService.send(requestDto.getEmail(), authToken);
    }

}
