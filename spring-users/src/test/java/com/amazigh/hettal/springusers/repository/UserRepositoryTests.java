package com.amazigh.hettal.springusers.repository;

import com.amazigh.hettal.springusers.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Add test for save user repository method")
    @Test
    public void givenUserObject_whenSaveUser_thenReturnUserObject() {
        // given
        User user = new User(
                "Hettal",
                "Amazigh",
                "amazighhettal@gmail.com",
                "password"
        );
        user.setCreatedAt(LocalDateTime.now());
        //when
        User savedUser = userRepository.save(user);

        //then
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }
}
