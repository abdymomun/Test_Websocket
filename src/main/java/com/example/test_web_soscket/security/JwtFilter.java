package com.example.test_web_soscket.controller.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader=request.getHeader("Authorization");
        if(tokenHeader!=null && tokenHeader.startsWith("Bearer ")){
            String token=tokenHeader.substring(7);
            if(StringUtils.hasText(token)) {

                try {
                    String email = jwtService.validateToken(token);

                    User user = userRepository.getUserByEmail(email)
                            .orElseThrow(
                                    () -> new NoSuchElementException(
                                            "Not exists user with email " + email));
                    SecurityContextHolder.getContext()
                            .setAuthentication(new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    null,
                                    user.getAuthorities()
                            ));

                }catch (JWTVerificationException e){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Token error");
                }
            }

        }
        filterChain.doFilter(request,response);
    }
}
