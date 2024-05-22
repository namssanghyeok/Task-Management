package sparta.task.security.filter;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import sparta.task.jwt.JwtUtil;
import sparta.task.model.User;
import sparta.task.model.UserRoleEnum;
import sparta.task.security.principal.UserPrincipal;
import sparta.task.security.service.UserDetailsServiceImpl;

import java.io.IOException;

/*
 * context 에 user 등록
 * 따라서 여기서 403 에러를 날릴 필요가 없음
 * 그러면 403 에러가 어디서 날라가야하나..?
 * 403 에러가 날라가긴 하는데... 난 내 커스텀 에러메시지를 날려주고 싶어.
 */
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        // NOTE: 만료되었거나, authorization header가 없는 경우 403 에러가 발생하지 않고,..
        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue) && jwtUtil.validateToken(tokenValue)) {
            // TODO: token이 만료되었다면, refresh-token을 확인하면됨

            Claims claims = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(claims);
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(Claims claims) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(claims);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(Claims claims) {
        UserDetails userDetails = new UserPrincipal(User.builder()
                .username(claims.getSubject())
                .id(claims.get(JwtUtil.CLAIM_ID, Long.class))
                .nickname(claims.get(JwtUtil.CLAIM_NICKNAME, String.class))
                .role(UserRoleEnum.valueOf(claims.get(JwtUtil.CLAIM_ROLE, String.class)))
                .build());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
