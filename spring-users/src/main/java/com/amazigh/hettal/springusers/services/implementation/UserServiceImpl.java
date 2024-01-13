package com.amazigh.hettal.springusers.services.implementation;

import com.amazigh.hettal.springusers.domain.JwtToken;
import com.amazigh.hettal.springusers.domain.User;
import com.amazigh.hettal.springusers.dto.SaveUserDto;
import com.amazigh.hettal.springusers.dto.UserDTO;
import com.amazigh.hettal.springusers.dtomapper.UserDTOMapper;
import com.amazigh.hettal.springusers.enums.Role;
import com.amazigh.hettal.springusers.exception.EmailAddressAlreadyExistsException;
import com.amazigh.hettal.springusers.exception.InvalidPasswordException;
import com.amazigh.hettal.springusers.exception.UserNotFoundException;
import com.amazigh.hettal.springusers.repository.JwtTokenRepository;
import com.amazigh.hettal.springusers.repository.UserRepository;
import com.amazigh.hettal.springusers.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private JwtTokenRepository jwtTokenRepository;

    // public UserServiceImpl() {}
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenRepository jwtTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder =  passwordEncoder;
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDTOMapper::fromUser)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(int id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return user.map(UserDTOMapper::fromUser).orElse(null);
    }

    @Override
    public UserDTO addNewUser(User user) {
        Optional<User> checkUserEmailExist = userRepository.findByEmail(user.getEmail());

        if (checkUserEmailExist.isPresent())
            throw new EmailAddressAlreadyExistsException("email address already in use");

        user.setCreatedAt(LocalDateTime.now());
        return UserDTOMapper.fromUser(userRepository.save(user));
    }

    @Override
    public void deleteUserById(int id) {
        Optional<User> checkUserExist = userRepository.findById(id);
        if (checkUserExist.isEmpty())
            throw new UserNotFoundException("User not found");

        User user = checkUserExist.get();
        // Get associated JwtTokens
        List<JwtToken> jwtTokens = user.getJwtTokens();

        jwtTokenRepository.deleteAll(jwtTokens);

        userRepository.deleteById(id);
    }

    @Override
    public UserDTO updatedUser(User user) {
        Optional<User> checkUserExist = userRepository.findById(user.getId());
        if (checkUserExist.isEmpty())
            throw new UserNotFoundException("User not found");

        return UserDTOMapper.fromUser(userRepository.save(user));
    }

    @Override
    public User registerNewUser(SaveUserDto newUser) {
        // Verify if e-mail already exist
        Optional<User> checkUserEmailExist = userRepository.findByEmail(newUser.getEmail());

        // If E-mail exist throw EmailAddressAlreadyExistsException
        if (checkUserEmailExist.isPresent())
            throw new EmailAddressAlreadyExistsException("email address already in use");
        // validate that the passwords entered by the user (when registering) are correct and match
        validatePassword(newUser);
        User user = new User();
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.ADMIN);
        return userRepository.save(user);
    }

    private void validatePassword(SaveUserDto newUser) {

        // verifica que campos password y repeatedPassword contengan texto
        if (!StringUtils.hasText(newUser.getPassword()) || !StringUtils.hasText(newUser.getRepeatedPassword())) {
            throw new InvalidPasswordException("Passwords do not match");
        }

        // verifica que campos password y repeatedPassword coincidan
        if (!newUser.getPassword().equals(newUser.getRepeatedPassword())) {
            throw new InvalidPasswordException("Passwords do not match");
        }
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
