package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.UserData;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserData userData;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userData.findByRegistration(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));


        return new UserPrincipal(user);
    }
}
