package backend.whereIsMyTeam.user.service;

import backend.whereIsMyTeam.email.EmailService;
import backend.whereIsMyTeam.exception.Jwt.AccessDeniedException;
import backend.whereIsMyTeam.exception.Jwt.InvalidRefreshTokenException;
import backend.whereIsMyTeam.exception.User.*;
import backend.whereIsMyTeam.oauth.*;
import backend.whereIsMyTeam.redis.*;
import backend.whereIsMyTeam.redis.domain.RedisKey;
import backend.whereIsMyTeam.user.dto.ReIssueRequestDto;
import backend.whereIsMyTeam.security.jwt.JwtTokenProvider;
import backend.whereIsMyTeam.user.UserRepository;
import backend.whereIsMyTeam.user.domain.Role;
import backend.whereIsMyTeam.user.domain.User;
import backend.whereIsMyTeam.user.dto.*;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;
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

    private final ProviderService providerService;


    /**
    * LoginRequestDto로 들어온 값을 통해 로그인 진행
    * 로컬 로그인
    * @param requestDto ,
    **/
    @Transactional
    public UserLoginResponseDto loginUser(@RequestBody UserLoginRequestDto requestDto) {
        // Email로 실제 저장된 유저인지 확인
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        //1. 패스워드 오류 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword()))
            throw new LoginFailureException();

        //토큰 발급 (Access, Refresh)
        String accessToken = jwtTokenProvider.createToken(requestDto.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        //Redis에 refresh 토큰 저장
        redisService.setDataWithExpiration(RedisKey.REFRESH.getKey() +user.getEmail(), refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        return new UserLoginResponseDto(user.getUserIdx(), accessToken, refreshToken,user.getEmail(),user.getNickName(),user.getProfileImgIdx());
    }


    /**
     * Refresh 토큰으로 Access토큰 재발행
     * @param requestDto
     * @return
     * 조건: access 토큰이 만료 되었을때
     */
    @Transactional
    public ReIssueResponseDto reIssue(@RequestBody ReIssueRequestDto requestDto) {

        /* Access Token 진짜 만료된건지는 생략
        Authentication authentication = jwtTokenProvider.getAuthentication(requestDto.getAccessToken());
        System.out.println("authentication은 정말 email일까? " + authentication);*/

        //Access 토큰 재발급
        User user = userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotFoundException::new);
        String accessToken = jwtTokenProvider.createToken(user.getEmail());

        return new ReIssueResponseDto(accessToken);
    }

    /**
     * Refresh 토큰으로 Access토큰 재발행
     * @param requestDto
     * @return
     * 조건: access 토큰이 만료 되었을때
     */
   /*
   @Transactional

    public TokenResponseDto reIssue(@RequestBody ReIssueRequestDto requestDto) {

        //Access 토큰 만료시간 검증
        // 만료되지 않은 상태로 토큰을 재발급시키지 x
        if(!jwtTokenProvider.validateTokenExpiration(requestDto.getAccessToken()))
            throw new AccessDeniedException();

        //Access,Refresh 토큰값 읽어오기
        String findRefreshToken = redisService.getData(RedisKey.REFRESH.getKey()+requestDto.getEmail());



        //Redis에서 꺼내온 값이 없음 or 사용자가 가져온 Refresh토큰값이 Redis랑 다르다-> 예외처리
        //유효한 refresh 토큰이 아님
        if (findRefreshToken == null || !findRefreshToken.equals(requestDto.getRefreshToken()))
            throw new InvalidRefreshTokenException();

        // Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(requestDto.getAccessToken());
        System.out.println("authentication은 정말 email일까? " + authentication);
        //User user = userRepository.findByEmail(requestDto.getEmail())
        //        .orElseThrow(UserNotFoundException::new);
         */
        //Access 토큰, Refresh 토큰 재발급
        //String accessToken = jwtTokenProvider.createToken(requestDto.getAccessToken()/*user.getEmail()*/);
       /* String refreshToken = jwtTokenProvider.createRefreshToken();

        //바뀐 토큰값을 Redis에 key-value 재설정
        redisService.setDataWithExpiration(RedisKey.REFRESH.getKey() + authentication,
                refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        return new TokenResponseDto(accessToken, refreshToken);
    }*/

    /**
     * Dto로 들어온 값을 통해 회원가입을 진행
     * @param requestDto
     * @return
     */
    @Transactional
    public UserRegisterResponseDto registerUser(UserRegisterRequestDto requestDto) {

        //User 객체 저장
        User user = userRepository.save(
                User.builder()
                        .email(requestDto.getEmail())
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .nickName(requestDto.getNickName())
                        .roles(Collections.singletonList(Role.ROLE_NOTAUTH))
                        .provider(null)
                        .build());



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

    /**
     * 소셜 로그인 구현
     * @param code
     * @param provider
     * @return
     */
    @Transactional
    public UserLoginResponseDto loginUserByProvider(String code, String provider) {

        //Authentication 코드로부터 Access 토큰 발급 받음
        AccessToken oauthAccessToken = providerService.getAccessToken(code, provider);

        //구글 Access토큰으로 프로필 요청
        ProfileDto profile = providerService.getProfile(oauthAccessToken.getValue(), provider);

        //9t 서버 Refresh토큰 발급
        String refreshToken = jwtTokenProvider.createRefreshToken();
        //refresh 토큰 set data+redis에 저장
        redisService.setDataWithExpiration(RedisKey.REFRESH.getKey() +profile.getEmail(), refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        //9t 서버 access토큰 발급
        String accessToken=jwtTokenProvider.createToken(profile.getEmail());

        //이 이메일로 처음 로그인 했는지 여부 확인
        Optional<User> findUser = userRepository.findByEmailAndProvider(profile.getEmail(), provider);
        if (findUser.isPresent()) { //이미 존재하는 회원
            User user = findUser.get();
            return new UserLoginResponseDto(user.getUserIdx(),accessToken,refreshToken,user.getEmail(),user.getNickName(),user.getProfileImgIdx());
        } else {
            //첫 소셜 로그인 => DB에 회원등록
            User saveUser = saveUser(profile, provider);
            return new UserLoginResponseDto(saveUser.getUserIdx(),accessToken, refreshToken,saveUser.getEmail(),saveUser.getNickName(),saveUser.getProfileImgIdx());
        }
    }

    /**
     * 소셜 로그인에서 회원가입
     * @param profile
     * @param provider
     * @return
     */
    private User saveUser(ProfileDto profile, String provider) {
        //이메일 숫자로 변경해서 닉네임으로 사용
        //완전하게 중복 피한다는 보장 x
        String e= profile.getEmail();;
        Long num=Long.parseLong(e);

        User user = User.builder()
                .email(profile.getEmail())
                .password(null)
                .nickName("팀원"+String.valueOf(num))
                .roles(Collections.singletonList(Role.ROLE_AUTH))
                .provider(provider)
                .build();

        User save = userRepository.save(user);
        return save;
    }

    /**
     * 로그아웃
     * @param requestDto
     */
    public void logout (HttpServletRequest header, UserLogoutRequestDto requestDto) {

        String accessToken=jwtTokenProvider.resolveToken(header);
        User user=userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new);

        //KEY값 이용해 Refresh 토큰값 읽어오기
        String findRefreshToken = redisService.getData(RedisKey.REFRESH.getKey()+user.getEmail());

        //Redis에서 꺼내온 값이 있다면 refresh 토큰 redis에서 삭제(없다면 만료시간 끝난게아닐까, 어차피 지워야하니까 재발급x)
        if (findRefreshToken != null)
            redisService.deleteData(RedisKey.REFRESH.getKey()+user.getEmail());

        //access token redis 블랙리스트 처리
        redisService.setDataWithExpiration(accessToken,"logout", JwtTokenProvider.TOKEN_VALID_TIME);

    }

}
