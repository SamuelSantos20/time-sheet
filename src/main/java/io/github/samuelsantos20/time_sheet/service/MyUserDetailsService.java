package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user: {}", username);
        User user = userService.findByRegistration(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new UserPrincipal(user);
    }
}