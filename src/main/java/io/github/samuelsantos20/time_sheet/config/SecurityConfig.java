package io.github.samuelsantos20.time_sheet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {



    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.csrf(AbstractHttpConfigurer :: disable)

                .formLogin(formlogin -> formlogin.loginPage("/login").permitAll())

                .authorizeHttpRequests(authorization->
                        authorization.requestMatchers("/employee/**").authenticated()
                        .requestMatchers("/manager/**").authenticated()
                        .requestMatchers("/timesheet/**").authenticated()
                        .requestMatchers("/workEntry/**").authenticated()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/approval/**").authenticated()



                )


                .build();

    }



    @Bean
    GrantedAuthorityDefaults grantedAuthority(){

        return new GrantedAuthorityDefaults("");
    }


}
