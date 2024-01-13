package com.amazigh.hettal.springusers.services.implementation.auth;

import com.amazigh.hettal.springusers.domain.JwtToken;
import com.amazigh.hettal.springusers.domain.User;
import com.amazigh.hettal.springusers.dto.AuthenticationRequestDto;
import com.amazigh.hettal.springusers.dto.RegisterUserDTO;
import com.amazigh.hettal.springusers.dto.SaveUserDto;
import com.amazigh.hettal.springusers.dtomapper.UserDTOMapper;
import com.amazigh.hettal.springusers.exception.UserAuthenticationException;
import com.amazigh.hettal.springusers.repository.JwtTokenRepository;
import com.amazigh.hettal.springusers.services.auth.AuthenticationService;
import com.amazigh.hettal.springusers.services.auth.JwtService;
import com.amazigh.hettal.springusers.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtService jwtServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRepository jwtTokenRepository;

    public RegisterUserDTO register(SaveUserDto newUser) {
        User user = userService.registerNewUser(newUser);

        // String jwt = jwtServiceImpl.generateToken(user, generateExtraClaims(user));

        // when a user registers, token is saved in DB with the id of the user to whom the token belongs
        // saveUserToken(user, jwt);

        return UserDTOMapper.fromUserToRegisterUserDTO(user);
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        // add firstName to the extraClaims (jwt payload info) that will be added to the token
        extraClaims.put("firstName", user.getFirstName());
        extraClaims.put("lastName", user.getLastName());
        // add the name of the user's assigned role to the extraClaims (jwt payload info) that will be added to the token
        // extraClaims.put("role", user.getRole().name());
        // add the name of the user's permissions to the extraClaims (jwt payload info) that will be added to the token
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;
    }

    public String login(AuthenticationRequestDto authRequest) {
        // creates an "authentication" token with the username and password entered by the user who is logging in
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        // if authentication fail throw an exception
        if (!authentication.isAuthenticated()) throw new UserAuthenticationException("Authentication failed");

        // after authenticating the user, look for the details of that user to send them to the jwtService and thus create the jwt token with their data (of the logged-in user)
        Optional<User> optionalUser = userService.findOneByUsername(authRequest.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // generate jwt token with the logged-in user data
            String jwt = jwtServiceImpl.generateToken(user, generateExtraClaims(user));

            // when a user logs in, the token is saved in DB with the id of the user to whom the token belongs
            saveUserToken(user, jwt);

            return jwt;
        }

        // Return null or handle the case when user details are not present
        return null;
    }

    public void logout(HttpServletRequest request) {
        // get the jwt token from the request (when user logs out)
        String jwt = jwtServiceImpl.extractJwtFromRequest(request);
        // if token is null or contains no text, returns control to the filter chain
        if (!StringUtils.hasText(jwt)) return;

        // get jwt token from db
        Optional<JwtToken> token = jwtTokenRepository.findByToken(jwt);
        // verify that token is present and that it is a valid token
        if (token.isPresent() && token.get().isValid()) {
            // invalidates the token to logout
            // token.get().setValid(false);
            // save the changes made to the token in the db
            // jwtTokenRepository.save(token.get());
            // delete the token from db
            jwtTokenRepository.delete(token.get());
        }
    }

    public Boolean validateToken(String jwt) {
        try {
            // extracts the username from the jwt, by doing so, you are validating that the token is valid:
            // check that the format is correct, validate the signature and if the token has expired
            jwtServiceImpl.extractUsername(jwt);
            // if you manage to extract the username from the jwt, the token is valid and returns true
            return true;

        } catch (Exception e) {
            // if it fails to extract the username from the jwt, then the token is not valid and false is returned
            return false;
        }
    }

    private void saveUserToken(User user, String jwt) {

        JwtToken jwtToken = new JwtToken();
        jwtToken.setUser(user);
        jwtToken.setToken(jwt);
        jwtToken.setExpirationDate(jwtServiceImpl.extractExpirationDate(jwt));
        jwtToken.setValid(true);

        jwtTokenRepository.save(jwtToken);
    }
}
