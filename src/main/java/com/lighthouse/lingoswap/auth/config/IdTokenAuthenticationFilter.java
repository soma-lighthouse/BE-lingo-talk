package com.lighthouse.lingoswap.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class IdTokenAuthenticationFilter extends OncePerRequestFilter {

    private final List<String> includedPatterns = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/signup",
            "/api/v1/form/**");
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final AuthenticationManager authenticationManager;
    private final AuthenticationFailureHandler failureHandler;
    private final GoogleIdTokenResolver idTokenResolver;

    @Override
    protected void doFilterInternal(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response, @NotNull final FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = idTokenResolver.resolve(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }
            Authentication authRequest = new IdTokenAuthenticationToken(token);
            Authentication authResult = authenticationManager.authenticate(authRequest);
            successfulAuthentication(authResult);
        } catch (AuthenticationException ex) {
            unsuccessfulAuthentication(request, response, ex);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void successfulAuthentication(final Authentication authResult) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
    }

    private void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    @Override
    protected boolean shouldNotFilter(@NotNull final HttpServletRequest request) {
        return includedPatterns.stream()
                .noneMatch(p -> antPathMatcher.match(p, request.getServletPath()));
    }

}
