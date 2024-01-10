package com.amazigh.hettal.springusers.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserDto implements Serializable {
    @Size(min = 5)
    private String firstName;

    @Size(min = 5)
    private String lastName;

    @NotEmpty(message = "E-mail cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    @Column(unique = true)
    private String email;

    @Size(min = 8) // clave debe tener al menos 8 digitos
    private String password;

    @Size(min = 8)
    private String repeatedPassword;
}
