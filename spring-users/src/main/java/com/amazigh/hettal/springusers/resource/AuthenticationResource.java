package com.amazigh.hettal.springusers.resource;

import com.amazigh.hettal.springusers.domain.HttpResponse;
import com.amazigh.hettal.springusers.dto.AuthenticationRequestDto;
import com.amazigh.hettal.springusers.dto.RegisterUserDTO;
import com.amazigh.hettal.springusers.dto.SaveUserDto;
import com.amazigh.hettal.springusers.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationResource {

    private final AuthenticationService authenticationServiceImpl;

    @PostMapping("/authenticate")
    public ResponseEntity<HttpResponse> authenticate(@RequestBody @Valid AuthenticationRequestDto authRequest) {
        System.out.println("authRequest ddddddd" + authRequest);
        String jwt = authenticationServiceImpl.login(authRequest);

        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("User created successfully")
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.ACCEPTED)
                        .data(Map.of("token", jwt))
                        .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@RequestBody @Valid SaveUserDto newUser) {
        RegisterUserDTO registeredUserDto = authenticationServiceImpl.register(newUser);
        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("User created successfully")
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.ACCEPTED)
                        .data(Map.of("user", registeredUserDto))
                        .build()
        );
    }

    // endpoint to validate the jwt token
    @GetMapping("/validate")
    private ResponseEntity<Boolean> validateToken(@RequestParam String jwt) {
        Boolean isTokenValid = authenticationServiceImpl.validateToken(jwt);
        return ResponseEntity.ok(isTokenValid);
    }

    @PostMapping("/logout")
    private ResponseEntity<HttpResponse> logout(HttpServletRequest request) {
        authenticationServiceImpl.logout(request);
        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("User created successfully")
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.ACCEPTED)
                        .data(Map.of("message", "Successful logout"))
                        .build()
        );
    }

    private URI location() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users").toUriString());
    }
}
