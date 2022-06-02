package com.example.twitter.security;

import com.example.twitter.exception.ApiRequestException;
import com.example.twitter.repository.UserRepository;
import com.example.twitter.repository.projection.user.UserPrincipalProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service("userDetailsServiceImpl")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPrincipalProjection user = userRepository.findUserPrincipalByEmail(username).orElseThrow(() ->
                new ApiRequestException("User not found", FORBIDDEN));
        if (user.getActivationCode() != null) {
            throw new LockedException("email not activated");
        }
        return new UserPrincipal(user.getId(), user.getEmail(), user.getPassword());
    }
}
