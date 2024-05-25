package sparta.task.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sparta.task.model.User;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_ID = "id";
    public static final String CLAIM_NICKNAME = "nickname";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    //
    public static final String JWT_ERROR_KEY = "expired";


    // 토큰 만료시간
    private final long ACCESS_EXPIRATION_TOKEN_TIME = 10 * 1000L; // 10초
//    private final long ACCESS_EXPIRATION_TOKEN_TIME = 60 * 60 * 1000L; // 60분
    private final long REFRESH_EXPIRATION_TIME = ACCESS_EXPIRATION_TOKEN_TIME * 24 * 7;


    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createAccessToken(User user) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(user.getUsername()) // 사용자 식별자값(ID)
                .setExpiration(new Date(date.getTime() + ACCESS_EXPIRATION_TOKEN_TIME)) // 만료 시간
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                // claims
                .claim(CLAIM_ROLE, user.getRole()) // 사용자 권한
                .claim(CLAIM_ID, user.getId())
                .claim(CLAIM_NICKNAME, user.getNickname())
                .compact();
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token, HttpServletRequest request) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            request.setAttribute("expired", true);
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    static public boolean isExpiredAccessToken(HttpServletRequest request) {
        return request.getAttribute("expired") != null && (Boolean) request.getAttribute("expired");
    }
}
