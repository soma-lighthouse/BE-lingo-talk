package com.lighthouse.lingoswap.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;
    private final IdTokenAuthenticationFilter idTokenAuthenticationFilter;
    private final LoggingFilter loggingFilter;

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .authenticationManager(authenticationManager)
                .addFilterBefore(loggingFilter, DisableEncodeUrlFilter.class)
                .addFilterAt(idTokenAuthenticationFilter, BasicAuthenticationFilter.class)
                .addFilterAt(bearerTokenAuthenticationFilter, BasicAuthenticationFilter.class)
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/token").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/**", "/.well-known/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .anonymous().disable()
                .logout().disable();

        return http.build();
    }

}
