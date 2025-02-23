package io.github.samuelsantos20.time_sheet.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class InMemoryAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.debug("InMemoryAuthenticationProvider: Attempting to authenticate user: {}", authentication.getName());
        String username = authentication.getName();

        //54321
        String password =  authentication.getCredentials().toString();

        log.info("Usuario: {}", username);



        if ("master".equals(username) && "54321".equals(password)){


            return new UsernamePasswordAuthenticationToken(username,password, List.of(new SimpleGrantedAuthority(("Gerente"))));

        }

        throw new BadCredentialsException("Usuário ou senha inválidos");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
