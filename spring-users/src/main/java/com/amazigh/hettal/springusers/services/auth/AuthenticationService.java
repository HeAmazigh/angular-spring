package com.amazigh.hettal.springusers.services.auth;

import com.amazigh.hettal.springusers.dto.AuthenticationRequestDto;
import com.amazigh.hettal.springusers.dto.RegisterUserDTO;
import com.amazigh.hettal.springusers.dto.SaveUserDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    public String login(AuthenticationRequestDto authRequest);
    public RegisterUserDTO register(SaveUserDto newUser);
    public void logout(HttpServletRequest request);
    public Boolean validateToken(String jwt);
}
