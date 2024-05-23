package sparta.task.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sparta.task.repository.UserRepository;
import sparta.task.security.principal.UserPrincipal;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    // JWT authentication filter 에서 manager 를 이용해 authenticate 메서드를 수행하면, 내부적으로 loadUserByUsername 가 실행됨.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserPrincipal(this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found")));
    }
}
