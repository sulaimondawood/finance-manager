package com.dawood.finance.configs;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dawood.finance.dtos.auth.ErrorResponse;
import com.dawood.finance.services.UserDetailsServiceImpl;
import com.dawood.finance.utils.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;
  private final UserDetailsServiceImpl userDetailsServiceImpl;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {

      String token = authHeader.substring(7);

      if (jwtUtils.validatetoken(token)) {

        String email = jwtUtils.getUsername(token);

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
            Collections.emptyList());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
      }

      filterChain.doFilter(request, response);

    } catch (UsernameNotFoundException e) {
      writeErrorResponse(response, "Username not found", HttpServletResponse.SC_NOT_FOUND);
      logger.error("Username not found " + e.getMessage());
    } catch (JwtException e) {
      logger.error("Invalid jwt token " + e.getMessage());
      writeErrorResponse(response, "Invalid authorization token", HttpServletResponse.SC_UNAUTHORIZED);
    } catch (Exception e) {
      logger.error("Something went wrong " + e.getMessage());
      writeErrorResponse(response, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

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
