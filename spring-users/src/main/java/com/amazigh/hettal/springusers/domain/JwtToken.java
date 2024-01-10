package com.amazigh.hettal.springusers.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3000) // token contains many characters, DB defaults to 256, should be increased
    private String token;

    // token expiration date
    private Date expirationDate;

    // indicates if token is valid
    private boolean isValid;

    @ManyToOne // a user can have many tokens, a token is assigned to a single user
    @JoinColumn(name = "user_id")
    private User user;
}
