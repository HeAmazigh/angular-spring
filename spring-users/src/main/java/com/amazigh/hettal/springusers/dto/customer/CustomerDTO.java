package com.amazigh.hettal.springusers.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDateTime createdAt;
}
