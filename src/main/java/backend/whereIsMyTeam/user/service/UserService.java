package backend.whereIsMyTeam.user.service;

//import backend.whereIsMyTeam.email.EmailService;
import backend.whereIsMyTeam.exception.Jwt.InvalidRefreshTokenException;
import backend.whereIsMyTeam.exception.User.EmailNotAuthenticatedException;
import backend.whereIsMyTeam.exception.User.LoginFailureException;
import backend.whereIsMyTeam.exception.User.UserNotFoundException;
import backend.whereIsMyTeam.redis.domain.RedisKey;
import backend.whereIsMyTeam.redis.RedisService;
import backend.whereIsMyTeam.redis.dto.ReIssueRequestDto;
import backend.whereIsMyTeam.security.jwt.JwtTokenProvider;
import backend.whereIsMyTeam.user.UserRepository;
import backend.whereIsMyTeam.user.domain.User;
import backend.whereIsMyTeam.user.dto.UserLoginRequestDto;
import backend.whereIsMyTeam.user.dto.UserLoginResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import backend.whereIsMyTeam.security.dto.TokenResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@MapperScan(basePackages = {"org.springframework.security.crypto.password.PasswordEncoder"})
@RequiredArgsConstructor
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RedisService redisService;
    //private final EmailService emailService;

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
        if (!user.getEmailAuth())
            throw new EmailNotAuthenticatedException();

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
}
