package backend.whereIsMyTeam.user;

import backend.whereIsMyTeam.email.EmailService;
import backend.whereIsMyTeam.exception.User.EmailAuthTokenNotFoundException;
import backend.whereIsMyTeam.exception.User.UserEmailAlreadyExistsException;
import backend.whereIsMyTeam.exception.User.UserNotExistException;
import backend.whereIsMyTeam.redis.RedisService;
import backend.whereIsMyTeam.security.JwtTokenProvider;
import backend.whereIsMyTeam.user.dto.EmailAuthRequestDto;
import backend.whereIsMyTeam.user.dto.UserRegisterRequestDto;
import backend.whereIsMyTeam.user.dto.UserRegisterResponseDto;
import backend.whereIsMyTeam.redis.domain.RedisKey;
import backend.whereIsMyTeam.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


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

    /**
     * Dto로 들어온 값을 통해 회원가입을 진행
     * @param requestDto
     * @return
     */
    @Transactional
    public UserRegisterResponseDto registerUser(UserRegisterRequestDto requestDto) {

        //이메일 중복 확인 이후에 api 따로 분리하기!
        validateDuplicated(requestDto.getEmail());

        String authToken = UUID.randomUUID().toString();
        //유효 시간 5분
        //key+이메일, 전달 데이터로
        redisService.setDataWithExpiration(RedisKey.EAUTH.getKey()+requestDto.getEmail(), authToken, 60*5L);

        //User 객체 저장
        User user = userRepository.save(
                User.builder()
                        .email(requestDto.getEmail())
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .nickName(requestDto.getNickName())
                        .emailAuth(false)
                        .profileImgIdx(1L)
                        .build());

        //이메일 전송
        emailService.send(requestDto.getEmail(), authToken);

        return UserRegisterResponseDto.builder()
                .userIdx(user.getUserIdx())
                .email(user.getEmail())
                .build();
    }

    //이미 존재하는 이메일인지 확인
    public void validateDuplicated(String email) {
        if (userRepository.findByEmail(email).isPresent())
            throw new UserEmailAlreadyExistsException();
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

}
