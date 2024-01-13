package com.amazigh.hettal.springusers.config.security;

import com.amazigh.hettal.springusers.domain.JwtToken;
import com.amazigh.hettal.springusers.domain.User;
import com.amazigh.hettal.springusers.exception.UserNotFoundException;
import com.amazigh.hettal.springusers.repository.JwtTokenRepository;
import com.amazigh.hettal.springusers.services.auth.JwtService;
import com.amazigh.hettal.springusers.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtServiceImpl;
    private final JwtTokenRepository jwtTokenRepository;
    private final UserService userService;
    public JwtAuthenticationFilter(JwtService jwtServiceImpl, JwtTokenRepository jwtTokenRepository, UserService userService) {
        this.jwtServiceImpl = jwtServiceImpl;
        this.jwtTokenRepository = jwtTokenRepository;
        this.userService = userService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Get authorization header and extract the token
        String jwt = jwtServiceImpl.extractJwtFromRequest(request);
        if (!StringUtils.hasText(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1.1 Obtain valid and NOT expired token from DB
        // we look in DB for the token extracted from the authorization header
        Optional<JwtToken> jwtToken = jwtTokenRepository.findByToken(jwt);

        boolean isValid = validateToken(jwtToken); // check that the token obtained is valid

        // if the token is not valid, we continue with the filter chain
        if (!isValid) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Obtain username (subject) from the token, doing so automatically validates the token format, its signature and expiration date
        String username = jwtServiceImpl.extractUsername(jwt);

        // 3. Define the "Authentication" object within the Security Context Holder
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found. Username: " + username));

        //implementation of the "Authentication" interface
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, // username extracted from token
                null, // "null" is provided for the password (credentials) because the authorization mechanism is in tokens and not in passwords
                user.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetails(request)); // get details of the request
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 5. Execute rest of filters
        filterChain.doFilter(request, response);
    }

    private boolean validateToken(Optional<JwtToken> jwtTokenOpt) {
        if (jwtTokenOpt.isEmpty()) {
            System.out.println("Method 'validateToken' says: Token does not exist or was not generated.");
            return false;
        }

        JwtToken jwtToken = jwtTokenOpt.get();

        // obtain current date from the system to validate the token expiration
        Date currentDate = new Date(System.currentTimeMillis());

        // check if token is valid
        // if the token expiration date is later than the current date, it means that the token is still valid
        boolean isValid = jwtToken.isValid() && jwtToken.getExpirationDate().after(currentDate);

        // performs token invalidation (sets it to false)
        if (!isValid) {
            updateTokenStatus(jwtToken);
        }
        return isValid;
    }

    private void updateTokenStatus(JwtToken jwtToken) {
        jwtToken.setValid(false); // makes the token invalid
        jwtTokenRepository.save(jwtToken); // save the changes made to the token in DB
    }
}
