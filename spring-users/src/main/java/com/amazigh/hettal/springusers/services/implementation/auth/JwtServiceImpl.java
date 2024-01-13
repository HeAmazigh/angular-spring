package com.amazigh.hettal.springusers.services.implementation.auth;

import com.amazigh.hettal.springusers.services.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    // constant that contains the number of minutes the token lasts
    @Value("${security.jwt.expiration_in_minutes}")
    private Long EXPIRATION_IN_MINUTES;

    // secret key with which the token is signed
    @Value("${security.jwt.secret_key}")
    private String SECRET_KEY;

    //To generate token only with user details
    public String generateToken(UserDetails user) {
        return generateToken(user, new HashMap<>());
    }

    public String generateToken(UserDetails user, Map<String, Object> extraClaims) {
        // current system date
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expirationDate = new Date((EXPIRATION_IN_MINUTES * 60 * 1000) + issuedAt.getTime());

        String jwt = Jwts.builder()
                // add additional claims that are not required
                .setClaims(extraClaims)
                // add username to jwt
                .setSubject(user.getUsername())
                // indicates token issuance date
                .setIssuedAt(issuedAt)
                // indicates token expiration date
                .setExpiration(expirationDate)
                // specifies token signing key and algorithm
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    private Key generateKey() {
        // byte array containing the secret key
        // byte[] keyBytes = SECRET_KEY.getBytes();
        // we use this when we have a key that was encoded in base 64
        byte[] decodedKey = Decoders.BASE64.decode(SECRET_KEY);
        // System.out.println("decodedKey " + decodedKey + " SECRET_KEY "+ SECRET_KEY + " " + Keys.hmacShaKeyFor(decodedKey));
        // generate SecretKey instance with HMAC-SHA algorithm
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String jwt) {
        // return extractClaim(jwt, Claims::getSubject);
        // extracts all the claims from the token, and then obtains the "subject" claim that contains the username
        return extractAllClaims(jwt).getSubject();
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder()
                // sets the signing key to verify the authenticity of the token
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(jwt)
                // obtains the payload of the token (the data)
                .getBody();
    }

    private <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        // 1. Get "Authorization" http header, which is who contains the token in the request
        String authorizationHeader = request.getHeader("Authorization");
        // 1.1 Validate that header contains text, that is, that the token comes in the header
        // also validates that the header begins with "Bearer", after that comes the token

        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            // if header has no text and does not contain the token, we return null
            return null;
        }

        // 2. Obtains token from the authorization header and returns it
        return authorizationHeader.split(" ")[1];
    }

    public Date extractExpirationDate(String jwt) {
        return extractAllClaims(jwt).getExpiration();
    }
}
