package com.amazigh.hettal.springusers.resource;

import com.amazigh.hettal.springusers.domain.HttpResponse;
import com.amazigh.hettal.springusers.domain.User;
import com.amazigh.hettal.springusers.dto.UserDTO;
import com.amazigh.hettal.springusers.exception.UserNotFoundException;
import com.amazigh.hettal.springusers.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getAllUsers() {
        List<UserDTO> usersDTO = userService.getAllUsers();
        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("List users")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("users", usersDTO))
                        .build()
                );
    }
    @PostMapping
    public ResponseEntity<HttpResponse> saveNewUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO savedUserDTO = userService.addNewUser(userDTO);
        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("User created successfully")
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.ACCEPTED)
                        .data(Map.of("user", savedUserDTO))
                        .build()
        );
    }

    @PutMapping
    public ResponseEntity<HttpResponse> updateUser(@Valid @RequestBody User user) {
        UserDTO updatedUserDTO = userService.updatedUser(user);
        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("User updated successfully")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("user", updatedUserDTO))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getUserById(@PathVariable int id) {
        Optional<UserDTO> userDTO = Optional.ofNullable(userService.getUserById(id));

        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("User info")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("user", userDTO))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteUserById(@PathVariable int id) {
        Optional<UserDTO> userDTO = Optional.ofNullable(userService.getUserById(id));

        if (userDTO.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        userService.deleteUserById(id);

        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("User deleted successfully")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    private URI location() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users").toUriString());
    }
}
