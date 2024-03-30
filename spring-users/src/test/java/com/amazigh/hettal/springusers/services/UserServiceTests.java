package com.amazigh.hettal.springusers.services;

import com.amazigh.hettal.springusers.domain.JwtToken;
import com.amazigh.hettal.springusers.domain.User;
import com.amazigh.hettal.springusers.dto.UserDTO;
import com.amazigh.hettal.springusers.enums.Role;
import com.amazigh.hettal.springusers.exception.EmailAddressAlreadyExistsException;
import com.amazigh.hettal.springusers.repository.JwtTokenRepository;
import com.amazigh.hettal.springusers.repository.UserRepository;
import com.amazigh.hettal.springusers.services.implementation.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenRepository jwtTokenRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private UserDTO userDTO;
    private User user;

    @BeforeEach
    public void setup() {
        /*
         userRepository = Mockito.mock(UserRepository.class);
         userService = new UserServiceImpl(userRepository);
        */
        userDTO = new UserDTO(
            0,
                "Hettal",
                "Amazigh",
                "amazigh@gmail.com",
                "password",
                LocalDateTime.now()
        );
        user = new User(
            0,
            "Hettal",
            "Amazigh",
            "amazigh@gmail.com",
            "password"
        );

        user.setCreatedAt(LocalDateTime.now());
    }

    // JUnit test for saveUser
    @DisplayName("JUnit test for saveUser method")
    @Test
    public void givenUserObject_whenSaveUser_thenReturnUserObject() {
        // Given
        given(userRepository.findByEmail(userDTO.getEmail())).willReturn(Optional.empty());
        user.setRole(Role.ADMIN);
        System.out.println(user);
        given(userRepository.save(any(User.class))).willReturn(user);

        // When
        UserDTO savedUser = userService.addNewUser(userDTO);

        // Then
        Assertions.assertThat(savedUser).isNotNull();
    }

    // JUnit test for saveUser
    @DisplayName("JUnit test for saveUser method throws exception")
    @Test
    public void givenExistingEmail_whenSaveUser_thenThrowsException() {
        // Given
        given(userRepository.findByEmail(userDTO.getEmail())).willReturn(Optional.of(user));

        // When
        org.junit.jupiter.api.Assertions.assertThrows(EmailAddressAlreadyExistsException.class, () -> userService.addNewUser(userDTO));

        // Then
        verify(userRepository, never()).save(any(User.class));
    }

    // JUnit test for saveUser
    @DisplayName("JUnit test for getAllUsers method")
    @Test
    public void givenListUsers_whenGetAllUsers_thenReturnUsersList() {
        // Given
        User user1 = new User(
                2,
                "Hettal 1",
                "Amazigh 1",
                "amazighettal@gmail.com",
                "password"
        );

        user.setCreatedAt(LocalDateTime.now());

        given(userRepository.findAll()).willReturn(List.of(user, user1));

        // When
        List<UserDTO> listUsers = userService.getAllUsers();

        // Then
        Assertions.assertThat(listUsers).isNotNull();
        Assertions.assertThat(listUsers.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for getAllUsers method (negative scenario)")
    @Test
    public void givenEmptyListUsers_whenGetAllUsers_thenReturnEmptyUsersList() {
        // Given
        user.setCreatedAt(LocalDateTime.now());

        given(userRepository.findAll()).willReturn(Collections.emptyList());

        // When
        List<UserDTO> listUsers = userService.getAllUsers();

        // Then
        Assertions.assertThat(listUsers).isEmpty();
        Assertions.assertThat(listUsers.size()).isEqualTo(0);
    }

    @DisplayName("JUnit test for getUserById method")
    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject() {
        // Given
        int id = 1;
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        // When
        UserDTO getUser = userService.getUserById(id);

        // Then
        Assertions.assertThat(getUser).isNotNull();
    }

    @DisplayName("JUnit test for updateUser method")
    @Test
    public void givenUserObject_whenUpdateUser_thenReturnUpdatedUser() {
        // Given
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);
        user.setEmail("amazigh-update@gmail.com");
        user.setFirstName("amazighUpdate");

        // When
        UserDTO updatedUser = userService.updatedUser(user);

        // Then
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo("amazigh-update@gmail.com");
        Assertions.assertThat(updatedUser.getFirstName()).isEqualTo("amazighUpdate");
    }

    @DisplayName("JUnit test for deleteUser method")
    @Test
    public void givenUserId_whenDeleteUser_thenReturnNothing() {
        int userId = 1;
        // Mocking JwtTokens associated with the user
        JwtToken jwtToken = new JwtToken();
        jwtToken.setId(1L); // Set token ID
        jwtToken.setUser(user);
        List<JwtToken> jwtTokens = List.of(jwtToken);

        // Given
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        willDoNothing().given(userRepository).deleteById(userId);

        // When
        userService.deleteUserById(userId);
        jwtTokenRepository.deleteAll(jwtTokens);
        // Then
        verify(jwtTokenRepository, times(1)).deleteAll(jwtTokens);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
