package sparta.task.infrastructure.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sparta.task.domain.model.UserRoleEnum;
import sparta.task.infrastructure.jwt.JwtUtil;
import sparta.task.infrastructure.security.exception.AccessDeniedHandlerImpl;
import sparta.task.infrastructure.security.exception.AuthenticationEntryPointImpl;
import sparta.task.infrastructure.security.filter.JwtAuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // manager 를 이용해 직접 token 생성
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil);
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.httpBasic().disable();
        http.formLogin().disable();

        http.sessionManagement((httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)));

        http.exceptionHandling(e -> e
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
        );

        http.headers(headers -> headers.frameOptions().disable());

        http.authorizeHttpRequests(requests ->
                requests.requestMatchers(HttpMethod.POST, "/api/user", "/api/user/").anonymous()
                        .requestMatchers(HttpMethod.POST, "/api/auth/reissue").anonymous()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").anonymous()
                        .requestMatchers("/admin/**").hasAuthority(UserRoleEnum.ADMIN.getAuthority())
                        // 테스트
                        .requestMatchers("/api/task/test", "/api/task/test/").permitAll()
                        .requestMatchers("/api/task/admin").hasAuthority(UserRoleEnum.ADMIN.getAuthority())
                        .requestMatchers("/api/task/user").hasAuthority(UserRoleEnum.USER.getAuthority())
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
