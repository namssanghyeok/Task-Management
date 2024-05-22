package sparta.task.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sparta.task.repository.UserRepository;
import sparta.task.security.principal.UserPrincipal;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserPrincipal(this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found")));
    }
}
