package com.MovieFlix.demo.auth.repositories;

import com.MovieFlix.demo.auth.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken , Integer> {

//    This will chekc refresh tokjen exist or not
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
