package com.amazigh.hettal.springusers.resource;

import com.amazigh.hettal.springusers.domain.User;
import com.amazigh.hettal.springusers.dtomapper.UserDTOMapper;
import com.amazigh.hettal.springusers.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@WebMvcTest
public class UserResourceTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    public void givenUserObject_whenSaveNewUser_thenReturnSavedUser() throws Exception {
       // given - precondition or setup
        User user = new User(
                "Hettal",
                "Amazigh",
                "amazighhettal@gmail.com",
                "password"
        );
        user.setCreatedAt(LocalDateTime.now());
        BDDMockito.given(userService.addNewUser(ArgumentMatchers.any(User.class)))
                .willAnswer((invocation) -> {
                    User userArg = invocation.getArgument(0);
                    return UserDTOMapper.fromUser(userArg);
                });

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)));

        // then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.user.firstName",
                        CoreMatchers.is("Hettal")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.user.email",
                        CoreMatchers.is("amazighhettal@gmail.com")));
    }
}
