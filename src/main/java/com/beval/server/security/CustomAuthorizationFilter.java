package com.beval.server.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private JwtTokenProvider tokenProvider;
//    private UserDetailsService userDetailsService;

    public CustomAuthorizationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
//        this.userDetailsService = new CustomUserDetailsService();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //TODO: remove path check
        //do not apply filter to this path
        if (!request.getServletPath().equals("/api/v1/auth/signin")) {
            try {
                String jwt = getJwtFromRequest(request);
                if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                    String username = tokenProvider.getUsernameFromJwtToken(jwt);
                    List<String> roles = (List<String>) tokenProvider.getClaims(jwt).get("roles");
                    roles.forEach(r -> log.info(r));
                    log.info("Claims {}", roles);
                    //UserDetails userDetails = userDetailsService.loadUserByUsername(username);

//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                            username, null, roles);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ex) {
                log.error("Could not set user authentication in security context", ex);
                response.setHeader("error", ex.getMessage());
                response.sendError(FORBIDDEN.value());
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
