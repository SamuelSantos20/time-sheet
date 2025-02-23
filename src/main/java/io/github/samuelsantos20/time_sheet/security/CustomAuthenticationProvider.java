package io.github.samuelsantos20.time_sheet.security;

import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    // Adicione esta anotação para evitar a criação imediata
    private final UserService userService;

    //private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.debug("CustomAuthenticationProvider: Attempting to authenticate user: {}", authentication.getName());


        var password = authentication.getCredentials().toString();

        var username = authentication.getName();


        Optional<User> userOptional = Optional.ofNullable(userService.findByRegistration(username).orElseGet(() -> null));

        if (userOptional.isEmpty()) {

            throw new UsernameNotFoundException("Usuario não localizado na base de dados! -> " + username);
        }

        User user = userOptional.get();

        System.out.println(List.of(user));

        //boolean passwordOk = passwordEncoder.matches(password, user.getPassword());

        if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return new CustomAuthentication(user);

        }

        throw new BadCredentialsException("Senha inválida para o usuário: " + username);


    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
