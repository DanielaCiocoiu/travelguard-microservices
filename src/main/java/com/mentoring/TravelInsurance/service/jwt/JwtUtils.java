package com.mentoring.TravelInsurance.service.jwt;

import com.mentoring.TravelInsurance.service.user.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
  @Value("${app.jwtSecret}")
  private String jwtSecret;
  @Value("${app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${app.jwtSecret}")
  public void setSecret(String jwtSecret) {
    this.jwtSecret = jwtSecret;
  }

  @Value("${app.jwtExpirationMs}")
  public void setJwtExpirationInMs(int jwtExpirationMs) {
    this.jwtExpirationMs = jwtExpirationMs;
  }


  // generate token for user
  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    Map<String, Object> claims = new HashMap<>();
    Collection<? extends GrantedAuthority> roles = userPrincipal.getAuthorities();
    if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
      claims.put("isAdmin", true);
    }
    if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
      claims.put("isUser", true);
    }
    return doGenerateToken(claims, userPrincipal.getUsername());
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {

    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
  }
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
            .parseClaimsJws(token).getBody().getSubject();
  }


  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken).getBody();
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

}
