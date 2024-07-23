package com.MovieFlix.demo.auth.repositories;

import com.MovieFlix.demo.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String username);
}
