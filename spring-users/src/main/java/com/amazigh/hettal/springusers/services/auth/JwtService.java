package com.amazigh.hettal.springusers.services.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public interface JwtService {
    public String generateToken(UserDetails user, Map<String, Object> extraClaims);
    public String generateToken(UserDetails user);
    public String extractUsername(String jwt);
    public String extractJwtFromRequest(HttpServletRequest request);
    public Date extractExpirationDate(String jwt);
}
