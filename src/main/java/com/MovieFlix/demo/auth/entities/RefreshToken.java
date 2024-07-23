package com.MovieFlix.demo.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @Column(nullable = false , length = 500)
    @NotBlank(message = "please enter refresh token value")
    private  Integer refreshToken;

    @Column(nullable = false)
    private  Integer  expirationToken;

    @OneToOne
    private  User user;



}
