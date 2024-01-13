package com.amazigh.hettal.springusers.config.security.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    // This method is called when a user tries to access a secured resource without being authenticated
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Set the content type of the response to application/json
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Set the status of the response to 401 Unauthorized
        response.setStatus(response.SC_UNAUTHORIZED);
        // Create a map to hold the error details
        Map<String, Object> body = new HashMap<>();
        body.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("payload", "You need to login first in order to perform this action.");
        // Create an ObjectMapper to convert the response body to JSON and write it to the response output stream
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
