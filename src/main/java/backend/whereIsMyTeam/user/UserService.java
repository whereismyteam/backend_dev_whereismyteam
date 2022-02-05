package backend.whereIsMyTeam.user;

import backend.whereIsMyTeam.email.EmailService;
import backend.whereIsMyTeam.exception.User.EmailAuthTokenNotFoundException;
import backend.whereIsMyTeam.exception.User.UserEmailAlreadyExistsException;
import backend.whereIsMyTeam.exception.User.UserNickNameAlreadyExistsException;
import backend.whereIsMyTeam.exception.User.UserNotExistException;
import backend.whereIsMyTeam.redis.RedisService;
import backend.whereIsMyTeam.user.dto.*;
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
                        .emailAuth(false)
                        .profileImgIdx(1L)
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
