package backend.whereIsMyTeam.security.jwt;

import backend.whereIsMyTeam.exception.Jwt.*;
import backend.whereIsMyTeam.redis.RedisService;
import backend.whereIsMyTeam.redis.domain.RedisKey;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@Setter
@RequiredArgsConstructor
public class JwtTokenProvider {
    //Access 토큰

    @Value("${jwt.secret}")
    private String secretKey;

    public static final long TOKEN_VALID_TIME = 1000L * 60 * 30; // 30분
    public static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    private final RedisService redisService;
    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //Access 토큰 생성
    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);    //토큰의 키=email(중복x)
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    /**
     * Access 토큰 파싱
     * @param jwt
     * @return
     */
    public Claims parseJwt(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();

        return claims;
    }

    //Refresh 토큰 생성
    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }


    //이메일을 얻기 위해 access 토큰을 디코딩
    public String getUserEmail(String token) {
        try {
            Claims claims = parseJwt(token);
            String s=claims.getSubject();
            return s;
        } catch(ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    /**
     *  Request의 Header에서 'Access 토큰 값' 가져옴
     *  없을 경우 null 반환
    **/
    public String resolveToken(HttpServletRequest request) {
        if(request.getHeader("ACCESS_TOKEN") != null )
            return request.getHeader("ACCESS_TOKEN");
        return null;
    }

    /**
    * Request의 Header에서 'Refresh 토큰 값'을 가져옵니다.
    * 클라이언트에서 'refreshToken' : value로 보내줌
     * 없을 경우 null 반환
    **/
    public String resolveRefreshToken(HttpServletRequest request) {
        if(request.getHeader("REFRESH_TOKEN") != null )
            return request.getHeader("REFRESH_TOKEN");
        return null;
    }


    //토큰의 유효성 검증 + 만료일자 확인
    public boolean validateTokenExpiration(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 로그인 이후 API들 access 토큰 validation 함수
     *
     **/
    public void validateAccess(HttpServletRequest header,String email){
        //access 토큰 null 아닌지 검증
        String accessToken= resolveToken(header);
        if(accessToken == null)
            throw new AccessNotComeException();
        //logout에서 블랙리스트 처리된 access 토큰 아닌지 검증
        String findLogoutToken = redisService.getData(accessToken);
        if(findLogoutToken!=null)
            throw new InvalidAccessTokenException();
        //access 토큰 만료 아닌지 검증
        if(validateTokenExpiration(accessToken))
            throw new GoToReIssueException();
        //access 토큰 유저
        if(!getUserEmail(accessToken).equals(email))
            throw new InvalidAccessTokenException();
    }

    //토큰으로 실DB 저장된 인증된 회원객체로->인증객체 얻기
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Access 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("authorization", "bearer "+ accessToken);
    }

}
