package com.amazigh.hettal.springusers.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class HttpSecurityConfig {

    private final AuthenticationProvider authProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                // disable csrf security (our app is stateless)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> {
                    // define a stateless application
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .cors(Customizer.withDefaults())
                // define authentication strategy (it is injected)
                .authenticationProvider(authProvider)
                // execute the "jwtAuthenticationFilter" before the "UsernamePasswordAuthenticationFilter" filter is executed
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(HttpSecurityConfig::requestMatchersBuilder)
                .build();
    }

    // authorization based on HTTP request matching using permissions (authorities)
    private static void requestMatchersBuilder(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authRequest) {
        // Public endpoint authorization: register, login and validate token
        authRequest.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authRequest.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
        // authRequest.requestMatchers(HttpMethod.POST, "/auth/validate").permitAll();
        authRequest.requestMatchers(HttpMethod.POST, "/error").permitAll();

        // to access any other endpoint on the system, you must be authenticated
        authRequest.anyRequest().authenticated();
    }


}
