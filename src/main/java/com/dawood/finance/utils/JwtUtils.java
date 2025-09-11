package com.dawood.finance.utils;

import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

  @Value("${jwt.secret-key}")
  private String jwtSecret;

  public String generateToken(UserDetails userDetails) {
    return Jwts
        .builder()
        .subject(userDetails.getUsername())
        .issuedAt(new Date())
        .expiration(new Date(new Date().getTime() + Duration.ofDays(1).toMillis()))
        .signWith(getSecretKey())
        .compact();
  }

  public Claims parseToken(String jwt) {
    try {
      return Jwts.parser()
          .verifyWith(getSecretKey())
          .build()
          .parseSignedClaims(jwt)
          .getPayload();

    } catch (JwtException e) {
      System.out.println(e.getMessage());
      throw new JwtException("Invalid jwt" + e.getMessage());
    }
  }

  public boolean validatetoken(String jwt) {
    try {

      Jwts.parser()
          .verifyWith(getSecretKey())
          .build()
          .parseSignedClaims(jwt);

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String getUsername(String jwt) throws Exception {
    return parseToken(jwt).getSubject();
  }

  private SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

}
