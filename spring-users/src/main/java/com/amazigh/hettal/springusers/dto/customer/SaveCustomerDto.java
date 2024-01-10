package com.amazigh.hettal.springusers.dto.customer;

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
public class SaveCustomerDto implements Serializable {
    //@Size(min = 3)
    private String firstname;

    //@Size(min = 3)
    private String lastname;
    
    @NotEmpty(message = "E-mail cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    @Column(unique = true)
    private String email;
}
