package com.MovieFlix.demo.auth.repositories;

import com.MovieFlix.demo.auth.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken , Integer> {
}
