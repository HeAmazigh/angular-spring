package com.amazigh.hettal.springusers.services;

import com.amazigh.hettal.springusers.domain.User;
import com.amazigh.hettal.springusers.dto.RegisterUserDTO;
import com.amazigh.hettal.springusers.dto.SaveUserDto;
import com.amazigh.hettal.springusers.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerNewUser(SaveUserDto user);
    Optional<User> findOneByUsername(String username);
    UserDTO addNewUser(User user);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(int id);
    void deleteUserById(int id);
    UserDTO updatedUser(User user);

}
