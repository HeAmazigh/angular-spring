package com.amazigh.hettal.springusers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
}
