package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.AuthenticationRequest;
import io.github.samuelsantos20.time_sheet.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/authenticate")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    @PostMapping
    @Operation(summary = "Autenticação", description = "Criação do token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token criado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Usuário não localizado!")
    })
    public ResponseEntity<Map<String, String>> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("username: {}", authenticationRequest.username());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.username(), authenticationRequest.password())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuário ou senha inválidos"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username());
        String jwt = jwtUtil.generateToken(userDetails);

        log.info("Token JWT gerado");

        return ResponseEntity.ok(Map.of("token", jwt));
    }


}
