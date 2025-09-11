package com.dawood.finance.configs;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dawood.finance.dtos.auth.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

  private final JwtFilter jwtFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.csrf((csrf) -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(cors -> cors.disable())
        .authorizeHttpRequests((request) -> request.requestMatchers("/auth/**").permitAll()
            .requestMatchers("/status").permitAll()
            .anyRequest().authenticated())
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(getAuthenticationEntryPoint())
            .accessDeniedHandler(getAccessDeniedHandler()))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();

  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  private AuthenticationEntryPoint getAuthenticationEntryPoint() {
    return (request, response, authException) -> {
      writeErrorResponse(response, "Authentication failed", HttpStatus.UNAUTHORIZED.value());
    };
  }

  private AccessDeniedHandler getAccessDeniedHandler() {
    return (request, response, auth) -> {
      writeErrorResponse(response, "User unauthorized", HttpStatus.FORBIDDEN.value());
    };
  }

  private void writeErrorResponse(HttpServletResponse response, String message, int status)
      throws JsonProcessingException, IOException {

    response.setContentType("application/json");
    response.setStatus(status);

    ErrorResponse error = new ErrorResponse();

    error.setStatus(status);
    error.setMessage(message);

    ObjectMapper mapper = new ObjectMapper();

    response.getWriter().write(mapper.writeValueAsString(error));
  }

}
