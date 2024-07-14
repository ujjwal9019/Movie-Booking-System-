package com.MovieFlix.demo.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

//This class is responsible for intrecting with controller and mapping the data
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
   private Integer movieId;


    @NotBlank(message = "Please Provide Movies Title")
    private String title;


    @NotBlank(message = "Please Provide Movies director")
    private String director;


    @NotBlank(message = "Please Provide Movies studio")
    private String studio;


    private Set<String> movieCast;

    private Integer releaseYear;


    @NotBlank(message = "Please Provide Movies poster")
    private String poster;

    @NotBlank(message = "please provide poster 's url")
    private String posterUrl;
}
