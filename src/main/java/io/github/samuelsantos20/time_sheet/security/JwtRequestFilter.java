package io.github.samuelsantos20.time_sheet.security;

import io.github.samuelsantos20.time_sheet.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        log.debug("Authorization Header: {}", authorizationHeader);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7).trim();
            if (jwt.isEmpty()) {
                log.warn("Token JWT vazio recebido");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Empty JWT token");
                return;
            }
            try {
                username = jwtUtil.extractUsername(jwt);
                log.debug("Extracted Username: {}", username);
            } catch (Exception e) {
                log.error("Erro ao extrair username do JWT: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token: " + e.getMessage());
                return;
            }
        } else {
            log.debug("Cabeçalho Authorization inválido ou ausente");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Usuário autenticado: {}", userDetails.getUsername());
                } else {
                    log.warn("Token JWT inválido para usuário: {}", username);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                    return;
                }
            } catch (Exception e) {
                log.error("Erro na validação do token ou carregamento do usuário: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}