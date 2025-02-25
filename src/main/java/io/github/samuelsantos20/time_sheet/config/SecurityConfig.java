package io.github.samuelsantos20.time_sheet.config;

import io.github.samuelsantos20.time_sheet.security.CustomAuthenticationProvider;
import io.github.samuelsantos20.time_sheet.security.InMemoryAuthenticationProvider;
import io.github.samuelsantos20.time_sheet.security.JwtRequestFilter;
import io.github.samuelsantos20.time_sheet.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final InMemoryAuthenticationProvider inMemoryAuthenticationProvider;

    public final CustomAuthenticationProvider customAuthenticationProvider;

    private final JwtRequestFilter jwtRequestFilter;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, MyUserDetailsService userDetailsService) throws Exception {


        return httpSecurity.csrf(AbstractHttpConfigurer::disable)

                .formLogin(Customizer.withDefaults())

                .authorizeHttpRequests(authorization ->
                        authorization.requestMatchers("/employee/**").authenticated()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/manager/**").authenticated()
                                .requestMatchers("/timesheet/**").authenticated()
                                .requestMatchers("/workEntry/**").authenticated()
                                .requestMatchers("/user/**").authenticated()
                                .requestMatchers("/approval/**").authenticated()
                                .requestMatchers("/authenticate/**").permitAll()
                                .anyRequest().authenticated()

                ).userDetailsService(userDetailsService)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

                .build();

    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthority() {

        return new GrantedAuthorityDefaults("");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.inMemoryAuthentication().withUser("master").password("54321").roles("Gerente");

        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);

        return authenticationManagerBuilder.build();
    }

}
