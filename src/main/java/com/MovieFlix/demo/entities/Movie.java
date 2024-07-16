package com.MovieFlix.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false , length = 200)
    @NotBlank(message = "Please Provide Movies Title")
    private String title;

    @Column(nullable = false )
    @NotBlank(message = "Please Provide Movies director")
    private String director;

    @Column(nullable = false )
    @NotBlank(message = "Please Provide Movies studio")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;

    @Column(nullable = false )
    @NotNull(message = "Please Provide Movies release Date")
    private Integer releaseYear;

    @Column(nullable = false )
    @NotBlank(message = "Please Provide Movies poster")
    private String poster;



}
