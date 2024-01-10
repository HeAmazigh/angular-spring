package com.amazigh.hettal.springusers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDto implements Serializable {
    private String email;
    private String password;
}
