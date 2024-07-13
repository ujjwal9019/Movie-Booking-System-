package com.MovieFlix.demo.repositories;

import com.MovieFlix.demo.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRespository extends JpaRepository<Movie,Integer> {
}
